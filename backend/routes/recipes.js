'use strict';

let express = require('express');
let router = express.Router();

// include database model
let model = require('../models');

router.get("/", function(req, res) {
  model["recipe"]
      .findAll({raw: true}) // omits accessibility methods for data
      .then((stops) => {
        res.send(stops);
      });
});

module.exports = router;
