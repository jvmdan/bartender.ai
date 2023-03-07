# bartender.ai

## Introduction

A personal project that utilises machine learning (ML) to generate cocktail recipes. This is a first attempt at using the Java Deep Learning (JDL) library to produce an AI running primarily in the JVM. 

The idea is that the user will prompt the application with a phrase or set of words. The application will then, using a set of known-good cocktail recipes as training data, attempt to devise an entirely new recipe from the user prompt. 

## How It Works

A recipe is a list of ingredients, their respective quantities, and a set of instructions to make the cocktail. The application will attempt to learn which ingredients pair well together, in suitable quantities, and generate new recipe instances based upon this data & the given prompt.

Cocktails shall then be rated as good or bad by a real human being. If the cocktail is deemed to be "good" it may be added to the existing training data, further strengthening the model.


## Technology Stack

This application is built upon Java 17 & Jakarta EE. Spring has been chosen as the Inversion of Control (IoC) container, and Spring WebFlux provides a reactive RESTful API with an embedded Netty server. 

Spring has been chosen as the dependency injection (DI) framework. Some might consider this to be heavyweight in comparison to other third-party DI frameworks (such as Dagger or Ajave). Whilst I would be inclined to agree, the intention of this project is to be fun & easy to work on. Some performance tweaks have been applied to improve efficiency where possible.

## Upcoming Changes

* Add ability to convert between different units of measurement.
* Deploy to cloud infrastructure for test & development.
* Devise a suitable set of training data for the ML model.
* Convert H2 database to Redis (or other data store).
* Convert Spring MVC to WebFlux for efficiency reasons.
* Experiment with generating recipes using JDL.
* Add ability for users to rate created recipes.

## Known Issues

Almost everything is undeveloped at this point. At some point, I shall start to document known issues. For now, this merely serves as a reminder to write something here one day.