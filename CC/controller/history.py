from flask import Blueprint, jsonify

history_bp = Blueprint('history', __name__)

# Variabel global untuk menyimpan history
history = []

@history_bp.route('/history', methods=['GET'])
def get_history():
    try:
        return jsonify({"history": history})
    except Exception as e:
        return jsonify({"error": str(e)}), 500
