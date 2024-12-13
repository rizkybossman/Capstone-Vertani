import firebase_admin
from firebase_admin import credentials, firestore

# Inisialisasi Firebase
cred = credentials.Certificate("vertani-443915-2f6f5f668cea.json")  # Ganti path file
firebase_admin.initialize_app(cred)
db = firestore.client()
