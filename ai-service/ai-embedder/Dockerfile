FROM python:3.10-slim

RUN pip install --upgrade pip && \
    pip install flask sentence-transformers && \
    pip install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cpu

WORKDIR /app

COPY ./app /app
COPY ./models3 /app/models3

CMD ["python", "app.py"]