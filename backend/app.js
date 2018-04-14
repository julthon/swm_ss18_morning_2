'use strict';

let express = require("express");
let logger = require("morgan");

let recipesRouter = require("./routes/recipes");

// express app with middleware
let app = express();
app.use(logger("dev"));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

// configure routes here
app.use("/recipes", recipesRouter);

// catch 404
app.use(function(req, res) {
  res.sendStatus(404);
});

module.exports = app;
