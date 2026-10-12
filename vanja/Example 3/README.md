# Locust example

This examples demonstrates simple load testing of Spring Boot application from Example 2 directory using [Locust](https://docs.locust.io/en/latest/what-is-locust.html). [This article](https://medium.com/swlh/load-testing-with-locust-3e74349f9cbf) explains different metrics and shows how Locust could be used in advanced performance testing.

## Requirements

* Python 3
* requirements.txt

## Setup
1. Start Spring Boot application from Example 2 directory
   -  Follow instructions in README.md (Example 2 directory)
2. Setup virtual environment (Windows)
```console
python -m pip install virtualenv
python -m virtualenv venv
.\venv\Scripts\activate
python -m pip install -r requirements.txt
```
3. Uisng Python from virtual environemnt run
```console
python -m locust
```
4. Open browser and navigate to *localhost:8089*
5. Insert number of users (e.g. 10) and spawn rate (e.g. 1) and click *Start swarming* button
