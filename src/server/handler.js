const crypto = require('crypto');
const storeData = require('../services/storeData');
const predictTrash = require('../services/inferenceService');

async function postPredictTrash(request, h) {
  const { image } = request.payload;
  const { model } = request.server.app;

  const { label, certaintyPercentage, confidenceScore} = await predictTrash(model, image);
  const id = crypto.randomUUID();
  const createdAt = new Date().toISOString();
  const isAboveThreshold = parseFloat(certaintyPercentage) >= 90;

  const data = {
    "id": id,
    "result": label,
    "confidenceScore": certaintyPercentage,
    "isAboveThreshold": isAboveThreshold,
    "createdAt": createdAt
  }

  await storeData(id,data);

  const response = h.response({
    status: 'success',
    message: 'Model is predicted successfully.',
    data
  });
  response.code(201);
  return response;
}

module.exports = postPredictTrash;