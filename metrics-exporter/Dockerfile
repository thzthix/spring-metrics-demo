FROM python:3.11-slim

WORKDIR /app

# Copy requirements first for better Docker layer caching
COPY requirements.txt .

# Install Python dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy application code
COPY exporter.py .

# Expose metrics port
EXPOSE 8000

# Run the exporter
CMD ["python", "exporter.py"]