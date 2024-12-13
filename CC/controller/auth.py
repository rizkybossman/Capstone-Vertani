from flask import Blueprint, request, jsonify
from config.firebase import db

users_collection = db.collection('users')
auth_bp = Blueprint('auth', __name__)

@auth_bp.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    email = data.get('email')
    password = data.get('password')

    if not email or not password:
        return jsonify({"error": "Email dan password wajib diisi"}), 400

    existing_user = users_collection.where("email", "==", email).get()
    if existing_user:
        return jsonify({"error": "Email sudah digunakan"}), 400

    user_ref = users_collection.add({"email": email, "password": password})
    return jsonify({"message": "Registrasi berhasil", "user_id": user_ref[1].id}), 201

@auth_bp.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    email = data.get('email')
    password = data.get('password')

    if not email or not password:
        return jsonify({"error": "Email dan password wajib diisi"}), 400

    user = users_collection.where("email", "==", email).where("password", "==", password).get()
    if not user:
        return jsonify({"error": "Email atau password salah"}), 401

    user_id = user[0].id
    return jsonify({"message": "Login berhasil", "user_id": user_id}), 200
