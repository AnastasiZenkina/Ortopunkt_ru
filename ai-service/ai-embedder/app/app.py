from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer
import numpy as np

app = Flask(__name__)
model = SentenceTransformer("sentence-transformers/all-MiniLM-L6-v2")

def cosine_similarity(a, b):
    a = np.array(a)
    b = np.array(b)
    return float(np.dot(a, b) / (np.linalg.norm(a) * np.linalg.norm(b) + 1e-10))

@app.route('/health', methods=["GET"])
def health():
    return 'OK', 200

@app.route('/embed', methods=["POST"])
def embed():
    data = request.get_json()
    if not data or "texts" not in data:
        return jsonify({"error": "Missing 'texts' field"}), 400

    embeddings = model.encode(data["texts"], convert_to_numpy=True).tolist()
    return jsonify(embeddings)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5005)
