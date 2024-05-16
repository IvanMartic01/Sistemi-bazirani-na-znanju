import requests

def load_country_data():
    url = "http://localhost:8080/api/auth/load-country-data"
    try:
        response = requests.post(url)
        if response.status_code == 200:
            print("Country data loaded successfully.")
        else:
            print("Failed to load country data. Status code:", response.status_code)
    except Exception as e:
        print("Error occurred while loading country data:", str(e))

def load_user_data():
    url = "http://localhost:8080/api/auth/load-user-data"
    try:
        response = requests.post(url)
        if response.status_code == 200:
            print("User data loaded successfully.")
        else:
            print("Failed to load user data. Status code:", response.status_code)
    except Exception as e:
        print("Error occurred while loading user data:", str(e))

def load_event_data():
    url = "http://localhost:8080/api/event/load-data"
    try:
        response = requests.post(url)
        if response.status_code == 200:
            print("Event data loaded successfully.")
        else:
            print("Failed to load event data. Status code:", response.status_code)
    except Exception as e:
        print("Error occurred while loading event data:", str(e))

load_country_data()
load_user_data()
load_event_data()
