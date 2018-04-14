'use strict';

module.exports = function(sequelize, DataTypes) {
  const model = sequelize.define("recipe", {
    recipeId: {type: DataTypes.INTEGER, primaryKey: true},
    title: DataTypes.STRING,
    description: DataTypes.STRING
  });
  model.sync();
  return model;
};
