FROM python:3.10-slim

RUN pip install --upgrade pip && \
    pip install flask sentence-transformers && \
    pip install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cpu

COPY app /app
# COPY model /model

WORKDIR /app
CMD ["python", "app.py"]
