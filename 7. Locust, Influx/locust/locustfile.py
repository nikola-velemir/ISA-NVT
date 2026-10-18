from locust import HttpUser, task, between


class ClientDbExampleUser(HttpUser):
    wait_time = between(1, 5)
    host = "http://localhost:8082"

    measurements = ["Temperature", "Precipitation", "Humidity", "Irradiance"]

    @task
    def last_ten(self):
        for measurement in self.measurements:
            self.client.get(f"/measurements/10?name={measurement}")

    @task
    def last_record(self):
        for measurement in self.measurements:
            self.client.get(f"/measurements/last?name={measurement}")

    @task
    def aggregated_data(self):
        for measurement in self.measurements:
            self.client.get(f"/measurements/aggregated?name={measurement}")

    @task
    def deviation_from_mean(self):
        for measurement in self.measurements:
            self.client.get(f"/measurements/deviation?name={measurement}")

    def on_start(self):
        print("On start called")

    def on_stop(self):
        print("On stop called")
