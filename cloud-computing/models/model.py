import tensorflow as tf

# Memuat model machine learning
MODEL_PATH = 'soil_detection_model.h5'  # Ganti path model
try:
    model = tf.keras.models.load_model(MODEL_PATH)
    print(f"Model berhasil diload dari {MODEL_PATH}")
except Exception as e:
    print(f"Gagal meload model: {e}")
    exit()
