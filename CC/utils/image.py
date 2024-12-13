import numpy as np
import io
from PIL import Image

def preprocess_image(image_bytes):
    try:
        img = Image.open(io.BytesIO(image_bytes)).convert('RGB')
        img = img.resize((150, 150))  # Resize to match model input
        img_array = np.array(img) / 255.0  # Normalize (0-1)
        img_array = np.expand_dims(img_array, axis=0)  # Add batch dimension
        return img_array
    except Exception as e:
        raise ValueError(f"Error in image preprocessing: {e}")