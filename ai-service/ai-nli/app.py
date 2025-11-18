from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer, util

app = Flask(__name__)
model = SentenceTransformer("/app/models")

THRESHOLD = 0.35  # порог уверенности

@app.route("/nli", methods=["POST"])
def classify():
    data = request.get_json()
    premise = data.get("premise")
    hypotheses = data.get("hypotheses", [])

    if not premise or not hypotheses:
        return jsonify({"error": "Fields 'premise' and 'hypotheses' are required"}), 400

    try:
        results = {}
        emb1 = model.encode(premise, convert_to_tensor=True)
        for h in hypotheses:
            emb2 = model.encode(h, convert_to_tensor=True)
            sim = util.pytorch_cos_sim(emb1, emb2).item()
            results[h] = "yes" if sim > THRESHOLD else "no"

        return jsonify(results)
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8002)