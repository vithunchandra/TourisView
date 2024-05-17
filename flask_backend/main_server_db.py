from pyngrok import ngrok
import requests
import mysql.connector
from flask import Flask, request, send_from_directory, abort

from util_db import *

PORT = 5123
# ngrok.set_auth_token("")
ngrok.set_auth_token("2fwb0gdhrJi7uej5vbH3fcQS6ct_4Xe3HHQQV55WcoZ3b14CR")
subdomain = "decent-mullet-severely"


# https://2780-139-255-65-92.ngrok-free.app
mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="",
    database="proyek_mdp"
)
mycursor = mydb.cursor()


app = Flask(__name__)

def run_flask_app() :
    app.run(host='0.0.0.0', port=PORT)
    
@app.route('/login', methods=['POST'])
def login() :
    email = request.form['email']
    password = request.form['password']
    
    query_str = f"SELECT email, display_name as displayName from users where email='{email}' and password='{password}'"
    result = execute_select_with_cursor(mycursor, query_str)
    
    if len(result) > 0 :
        result_json = json.dumps(result[0])
        return result_json
    else :
        return abort(404, "Check your username/password again!")
    
@app.route('/register', methods=['POST'])
def register() :
    email = request.form['email']
    password = request.form['password']
    name = request.form['name']
    
    query_str = f"SELECT email, display_name from users where email='{email}'"
    result = execute_select_with_cursor(mycursor, query_str)
    print(result)
    
    if len(result) > 0 :
        return abort(400, "User already register!")
    else :
        str_query = f"INSERT INTO `users`(`email`, `password`, `display_name`) VALUES ('{email}','{password}','{name}')"
        str_result = execute_query(mydb, mycursor, str_query)
        
        query_str = f"SELECT email, display_name as displayName from users where email='{email}' and password='{password}'"
        result = execute_select_with_cursor(mycursor, query_str)
        result_json = json.dumps(result[0])
        
        return result_json

if __name__ == "__main__":
    ngrok_tunnel = ngrok.connect(PORT, domain= f"{subdomain}.ngrok-free.app")  # Assuming Flask app is running on port 5000
    print("Public URL:", ngrok_tunnel.public_url)

    run_flask_app()
    