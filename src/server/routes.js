const postPredictTrash = require('../server/handler');

const routes = [
  {
    path: '/predict',
    method: 'POST',
    handler: postPredictTrash,
    options: {
      payload: {
        allow: 'multipart/form-data',
        multipart: true
      }
    }
  }
]

module.exports = routes;