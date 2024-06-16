const tf = require('@tensorflow/tfjs-node');
const InputError = require('../exceptions/InputError');

async function predictTrash(model, image) {
  try {
    const tensor = tf.node
      .decodeJpeg(image)
      .resizeNearestNeighbor([ 150, 150])
      .expandDims()
      .toFloat()
      .div(tf.scalar(255));

    const prediction = model.predict(tensor);
    const predict = await prediction.dataSync()[0];

    let label,certaintyPercentage;


// console.log(score[0])
    if (predict >= 0.5){
      label = "Recycle";
      certaintyPercentage = (predict * 100).toFixed(2);
    } else {
      label = "Organic";
      certaintyPercentage = ((1-predict) * 100).toFixed(2);
    }
    return {label,certaintyPercentage};
  } catch (error){  
    throw new InputError("Terjadi kesalahan dalam melakukan prediksi");
  }
};
    
module.exports = predictTrash;
