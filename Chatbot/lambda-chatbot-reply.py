import requests
import json
from flask import Flask, request

# FB messenger credentials
ACCESS_TOKEN = "EAAWRbsZCcKJ4BADo8UBlOTKzWK4d7T6UKZCpACGjvGCH0moLPakgdJLqE0IsshQeTqhPwGQaYXyEsFF61AlC7o5ZBX3PCVweDn208q3RJ1AjqJYwuWSnQZB9kHwZAqm9PKNxTF5ZAqa6Wmw9kzPKQIxL3zzMZB58wgdl2v86P3nSCjkgrTd6srt"
url = "https://graph.facebook.com/v2.6/me/messages?access_token=" + ACCESS_TOKEN

app = Flask(__name__)

@app.route('/', methods=['POST'])
def handle_incoming_messages():
    data = request.json

    messaging = event['entry'][0]['messaging'][0]
    if 'message' in messaging:
        sender = event['entry'][0]['messaging'][0]['sender']['id']
        message = event['entry'][0]['messaging'][0]['message']['text']
        print("Sender", sender)
        print("Message", message)
    elif 'delivery' in messaging:
        sender = event['entry'][0]['messaging'][0]['sender']['id']
        watermark = event['entry'][0]['messaging'][0]['delivery']['watermark']
        print("Sender", sender)
        print("Watermark", watermark)

    sender = data['entry'][0]['messaging'][0]['sender']['id']
    message = data['entry'][0]['messaging'][0]['message']['text']

    # Prepare AI Request

    # Get Response from AI

    data = {
        "recipient": {"id": sender},
        "message": {"text": message}
    }

    request_messenger(data)

    return "ok"

def request_messenger(data):
    #response = requests.request("POST", url, headers=headers)
    r = requests.post(url, verify=False, json=data)
    print(r.status_code)
    r.json()

    return r

if __name__ == '__main__':
    app.run(debug=True)
