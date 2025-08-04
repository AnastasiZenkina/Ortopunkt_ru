from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer, util

app = Flask(__name__)

# Загружаем модель
from huggingface_hub import login


model = SentenceTransformer("MoritzLaurer/Multilingual-MiniLMv2-L6-mnli")

@app.route("/nli", methods=["POST"])
def classify():
    data = request.get_json()
    premise = data.get("premise")
    hypothesis = data.get("hypothesis")

    if not premise or not hypothesis:
        return jsonify({"error": "Both 'premise' and 'hypothesis' are required"}), 400

    # Используем zero-shot классификацию через cosine similarity
    try:
        prediction = model.predict([(premise, hypothesis)])
        return jsonify({"result": prediction})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8002)
