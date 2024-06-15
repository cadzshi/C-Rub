// Load model dari folder 'model/'
async function loadModel() {
    const model = await tf.loadLayersModel('model/model.json');
    return model;
}

// Fungsi untuk memprediksi gambar yang dipilih
async function predict() {
    const imageUpload = document.getElementById('imageUpload');
    const uploadedImage = document.getElementById('uploadedImage');
    const resultElement = document.getElementById('result');

    // Memuat model
    const model = await loadModel();

    // Memproses gambar yang dipilih
    const file = imageUpload.files[0];
    const reader = new FileReader();
    reader.onloadend = async function () {
        uploadedImage.src = reader.result;
        uploadedImage.style.display = 'inline'; // Tampilkan gambar yang diupload

        const image = new Image();
        image.src = reader.result;
        image.onload = async function () {
            // Resize gambar ke ukuran yang diharapkan oleh model
            const tensor = tf.browser.fromPixels(image)
                .resizeNearestNeighbor([150, 150])
                .expandDims()
                .toFloat()
                .div(tf.scalar(255));

            // Prediksi menggunakan model
            const predictions = await model.predict(tensor);
            
            // Mendapatkan nilai prediksi dari tensor hasil prediksi
            const predictionResult = predictions.dataSync()[0];

            // Mendeklarasikan variabel untuk label dan kepastian
            let label;
            let certaintyPercentage;

            // Menentukan label berdasarkan nilai prediksi
            if (predictionResult >= 0.5) {
                label = 'Recycle';
                certaintyPercentage = (predictionResult * 100).toFixed(2);
            } else {
                label = 'Organic';
                certaintyPercentage = ((1 - predictionResult) * 100).toFixed(2);
            }

            // Tampilkan hasil prediksi beserta nilai kepastian
            resultElement.innerText = `Prediction: ${label} (${certaintyPercentage}% confidence)`;

            // Hapus model dari memori
            model.dispose();
        }
    }
    if (file) {
        reader.readAsDataURL(file);
    }
}
