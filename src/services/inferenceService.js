const tf = require('@tensorflow/tfjs-node');
const InputError = require('../exceptions/InputError');

async function predictClassification(model, image) {
  try {
    const tensor = tf.node
      .decodeJpeg(image)
      .resizeNearestNeighbor([null, 224, 224, 3])
      .expandDims()
      .toFloat()
      .div(tf.scalar(255.0));

    const prediction = model.predict(tensor);
    const score = await prediction.data();

    let label,suggestion;

// console.log(score[0])
    if (score[0]> 0.5){
      label = "ORGANIK";
      suggestion = "Buang Sampah ke sini";
    } else {
      label = "NON-ORGANIK";
      suggestion = "Buang sampah ke sini ";
    }
    return {label,suggestion};
  } catch (error){  
    throw new InputError("Terjadi kesalahan dalam melakukan prediksi");
  }
}
    
module.exports = predictClassification;
