from flask import Flask, request, jsonify
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image
from tensorflow.keras.applications.mobilenet_v3 import preprocess_input
import numpy as np
import os
from PIL import UnidentifiedImageError

app = Flask(__name__)

# Load model
model = load_model('model_ikan.h5')

# Kelas sesuai urutan output model
classes = [
    'IkanBawal_AirTawar',
    'IkanGurame_AirTawar',
    'IkanKerapu_AirLaut',
    'IkanLepu_AirLaut',
    'IkanMas_AirTawar',
    'IkanSebelah_AirLaut'
]

# Folder upload sementara
UPLOAD_FOLDER = 'static/uploads'
os.makedirs(UPLOAD_FOLDER, exist_ok=True)

@app.route('/')
def home():
    return "API Klasifikasi Ikan Aktif"

@app.route('/predict', methods=['POST'])
def predict():
    if 'image' not in request.files:
        return jsonify({'error': 'No image uploaded'}), 400

    file = request.files['image']
    filepath = os.path.join(UPLOAD_FOLDER, file.filename)
    file.save(filepath)

    try:
        # Preprocessing sesuai training
        img = image.load_img(filepath, target_size=(224, 224))
        img_array = image.img_to_array(img)
        img_array = np.expand_dims(img_array, axis=0)
        img_array = preprocess_input(img_array)

        # Prediksi
        pred = model.predict(img_array)
        predicted_index = np.argmax(pred)
        predicted_class = classes[predicted_index]
        confidence = float(pred[0][predicted_index])

        return jsonify({
            'predicted_class': predicted_class,
            'confidence': confidence
        })

    except UnidentifiedImageError:
        return jsonify({'error': 'File bukan gambar yang valid'}), 400
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == "__main__":
    app.run("0.0.0.0", port=5000, debug=True)