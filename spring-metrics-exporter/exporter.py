from prometheus_client import start_http_server, Gauge
import time
import mysql.connector
import decimal

class Exporter:
    def __init__(self):
        self.users_count_by_group = Gauge('user_count_by_group', 'Number of user by group',['job'])
        self.total_users = Gauge('user_count', 'Number of user')
        self.db_config = {
            "host": "localhost",
            "port": 3306,
            "user": "asac",
            "password": "1234",
            "database": "asac"
        }
        try:
            self.db_connection = mysql.connector.connect(**self.db_config)
            self.db_cursor = self.db_connection.cursor()
            self.db_cursor.execute("SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED")
        except mysql.connector.Error as e:
            print(f"Error connecting to MySQL: {e}")
            self.db_connection = None
            self.db_cursor = None
            raise(e)
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
    