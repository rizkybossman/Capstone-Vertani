from flask import Blueprint, request, jsonify
from collections import OrderedDict
from datetime import datetime
import uuid
import numpy as np
from models.model_loader import model
from utils.image import preprocess_image
from config.firebase import db

predict_bp = Blueprint('predict', __name__)
predict_collection = db.collection('predict')

def suggest_crops(prediction_value):
    crops = ["Bayam", "Kangkung", "Sawi", "Tomat", "Terong", "Cabai", "Cabai Rawit"] if prediction_value > 0.5 else ["Selada Hidroponik", "Microgreens"]
    return np.random.choice(crops)

@predict_bp.route('/pred', methods=['POST'])
def predict():
    try:
        file = request.files.get('file')
        if not file or file.filename == '':
            return jsonify({"error": "No file uploaded"}), 400

        file_size = request.content_length
        if file_size > 1024 * 1024:
            return jsonify({"error": "File size exceeds 1MB"}), 400

        image_bytes = file.read()
        img_array = preprocess_image(image_bytes)

        predictions = model.predict(img_array).tolist()
        result = predictions[0][0]
        prediction_text = "Tanah terdeteksi" if result > 0.5 else "Tanah kurang subur"
        suggested_crop = suggest_crops(result)

        response = OrderedDict([
            ("id", str(uuid.uuid4())),
            ("prediction", prediction_text),
            ("soil_percentage", f"{result*100:.2f}%"),
            ("created_at", datetime.utcnow().strftime('%Y-%m-%d %H:%M:%S'))
        ])
        
        predict_collection.add(response)
        return jsonify(response)
    except Exception as e:
        return jsonify({"error": str(e)}), 500
