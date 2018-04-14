'use strict';

let fs        = require('fs');
let path      = require('path');
let Sequelize = require('sequelize');
let basename  = path.basename(__filename);

// database configuration happens here
let config    = {
  development: {
    dialect: "sqlite",
    storage: "./db.development.sqlite",
    operatorsAliases: false
  },
  production: {
    username: process.env.DB_USERNAME,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME,
    host:     process.env.DB_HOSTNAME,
    dialect: "postgres",
    operatorsAliases: false
  }
}.development;
// change to production for postgres

let db        = {};
db.sequelize = new Sequelize(config.database, config.username, config.password, config);

// add all models from current folder except current file
fs.readdirSync(__dirname).filter(file => {
  return (file.indexOf('.') !== 0) && (file !== basename) && (file.slice(-3) === '.js');
}).forEach(file => {
  let model = db.sequelize['import'](path.join(__dirname, file));
  db[model.name] = model;
});

Object.keys(db).forEach(modelName => {
  if (db[modelName].associate)
    db[modelName].associate(db);
});

db.Sequelize = Sequelize;

module.exports = db;
