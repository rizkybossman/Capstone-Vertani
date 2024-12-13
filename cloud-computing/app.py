from flask import Flask
from config.firebase import db
from controllers.auth import auth_bp
from controllers.history import history_bp
from controllers.predict import predict_bp

# Inisialisasi aplikasi Flask
app = Flask(__name__)

# Register blueprint dari setiap modul
app.register_blueprint(auth_bp)
app.register_blueprint(history_bp)
app.register_blueprint(predict_bp)

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=8001)
