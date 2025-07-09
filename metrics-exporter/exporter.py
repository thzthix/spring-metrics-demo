from prometheus_client import start_http_server, Gauge
import time
import psycopg2
import decimal
import os

class Exporter:
    def __init__(self):
        self.users_count_by_group = Gauge('user_count_by_group', 'Number of user by group',['job'])
        self.total_users = Gauge('user_count', 'Number of user')
        self.db_config = {
            "host": os.getenv("DB_HOST", "localhost"),
            "port": int(os.getenv("DB_PORT", "5432")),
            "user": os.getenv("DB_USER", "asac"),
            "password": os.getenv("DB_PASSWORD", "1234"),
            "database": os.getenv("DB_NAME", "asac")
        }
        self.db_connection = None
        self.db_cursor = None
        self.connect_with_retry()
    
    def connect_with_retry(self, max_retries=30, delay=2):
        """Connect to PostgreSQL with retry logic"""
        for attempt in range(max_retries):
            try:
                print(f"Attempting to connect to PostgreSQL (attempt {attempt + 1}/{max_retries})")
                self.db_connection = psycopg2.connect(**self.db_config)
                self.db_cursor = self.db_connection.cursor()
                self.db_cursor.execute("SET SESSION CHARACTERISTICS AS TRANSACTION ISOLATION LEVEL READ COMMITTED")
                print("Successfully connected to PostgreSQL!")
                return
            except psycopg2.Error as e:
                print(f"Connection failed: {e}")
                if attempt < max_retries - 1:
                    print(f"Retrying in {delay} seconds...")
                    time.sleep(delay)
                else:
                    print("Max retries reached. Exiting.")
                    raise e

    def collect_metrics(self):
        if self.db_connection is None or self.db_cursor is None:
            print("Database connection not established")
            return
        try:
            current_user = self.get_all_users()
            for job, count in current_user:
                if isinstance(count, (int, float, decimal.Decimal)):
                    value = float(count)
                else:
                    value = 0.0
                self.users_count_by_group.labels(job=job).set(value)
            total_users = self.get_all_users_count()
            if isinstance(total_users, (int, float, decimal.Decimal)):
                total_value = float(total_users)
            else:
                total_value = 0.0
            self.total_users.set(total_value)
            print(f"Metrics updated: total={total_users}, by_group={current_user}")
        except Exception as e:
            print(f"Error collecting metrics: {e}")
            # 트랜잭션 오류 시 롤백 및 재연결
            try:
                self.db_connection.rollback()
            except:
                pass
            try:
                self.connect_with_retry()
            except:
                pass

    def get_all_users(self):
        self.db_cursor.execute("SELECT job, COUNT(*) as count FROM users GROUP BY job")
        result = self.db_cursor.fetchall()
        return result

    def get_all_users_count(self):
        self.db_cursor.execute("SELECT COUNT(*) FROM users")
        result = self.db_cursor.fetchone()
        return result[0]
        

    
    def run(self):
        start_http_server(8000)
        while True:
            self.collect_metrics()
            time.sleep(1)
if __name__ == "__main__":
    exporter = Exporter()
    exporter.run()
    