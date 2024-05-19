from pyngrok import ngrok
import requests
import mysql.connector
from flask import Flask, request, send_from_directory, abort

from util_db import *

PORT = 5123
# ngrok.set_auth_token("")
ngrok.set_auth_token("2fwb0gdhrJi7uej5vbH3fcQS6ct_4Xe3HHQQV55WcoZ3b14CR")
subdomain = "decent-mullet-severely"
DOMAIN_NOW = f"{subdomain}.ngrok-free.app"

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

# @app.route('/image', methods=['GET'])
# def get_image():
#     filepath = request.form['filepath']
#     print(filepath)
#     picture_exist = os.path.exists(filepath)
#     print(picture_exist)
    
#     if picture_exist:
#         filepath = filepath.replace("./uploaded/","")
#         return send_from_directory('uploaded', filepath)  # Specify subfolder
#     else :
#         return abort(404)


@app.route('/image/<filename>', methods=['GET'])
def get_image(filename):
    picture_exist = os.path.exists(f"./uploaded/{filename}/img1.jpg")
    
    if picture_exist:
        return send_from_directory('uploaded', f"./{filename}/img1.jpg")  # Specify subfolder
    else :
        return abort(404)

# name = name, imageUri = image,
# description = description, latitude = latitude,
# longitude = longitude, like = 0,
# poster = poster
@app.route('/uploadDestination', methods=['POST'])
def upload_destination() :
    destination_name = request.form['name']
    destination_latitude = request.form['latitude']
    destination_longitude = request.form['longitude']
    destination_description = request.form['description']
    destination_like = request.form['like']
    destination_poster_username = request.form['poster']
    imageUri = request.form['imageUri']
    
    select_query_str = f"SELECT user_id from users where email = '{destination_poster_username}'"
    destination_poster = execute_select_with_cursor(mycursor, select_query_str)
    if len(destination_poster) < 1  :
        return abort(404, "User not found")
    destination_poster = destination_poster[0]['user_id']
    
    select_query_str = f"SELECT destination_id from destinations where destination_name = '{destination_name}'"
    temp_check_len = execute_select_with_cursor(mycursor, select_query_str)
    if len(temp_check_len) > 0  :
        return abort(400, "Tourism place already exist")
    
    # replace_dest_name = str(destination_name).replace(" ","_").lower()
    # folder_path_now = f"./uploaded/{replace_dest_name}/"
    # if not os.path.exists(folder_path_now):
    #     os.mkdir(folder_path_now)
    # img_path_now = folder_path_now + "img1." + imageUri.split(".")[-1]
    
    # img_data = requests.get(imageUri).content
    # with open(img_path_now, 'wb') as handler:
    #     handler.write(img_data)
        
    sql_query = f"INSERT INTO `destinations`(`destination_name`, `destination_latitude`, `destination_longtitude`, `destination_description`, `destination_poster`, `destination_like`) VALUES ('{destination_name}','{destination_latitude}','{destination_longitude}','{destination_description}','{destination_poster}','{destination_like}')"
    temp_res = execute_query(mydb=mydb, mycursor=mycursor, query=sql_query)
    
    inserted_destination = f"SELECT * from destinations where destination_name='{destination_name}'"
    inserted_destination = execute_select_with_cursor(mycursor, inserted_destination)[0]
    
    
    sql_query = f"INSERT INTO `images`(`destination_id`, `image`) VALUES ('{inserted_destination['destination_id']}','{imageUri}')"
    temp_res = execute_query(mydb=mydb, mycursor=mycursor, query=sql_query)
    
    select_destination = f"SELECT `destination_name` as name, ifnull(`destination_latitude`,0) as latitude, ifnull(`destination_longtitude`,0) as longtitude, `destination_description` as description, (SELECT email from users where users.user_id = destination_poster) as poster, `destination_like` as 'like' FROM `destinations` WHERE destination_name = '{destination_name}'"
    dst_now = execute_select_with_cursor(mycursor, select_destination)[0]
    
    select_destination_img = f"SELECT * FROM `images` WHERE destination_id = (SELECT destination_id from destinations where destination_name = '{destination_name}') LIMIT 1"
    destination_img_now = execute_select_with_cursor(mycursor, select_destination_img)[0]
    # if not destination_img_now.startswith(('http://', 'https://')) :
    #     return abort(500, "MUST BE URL")
    
    dst_now.update({"imageUri":destination_img_now['image']})
    
    json_result_now = {
        "message":temp_res,
        "data": dst_now
    }
    
    # print(sql_query)
    return json_result_now

@app.route('/getAllDestination', methods=['GET'])
def get_all_destination() :
    sql_query = "SELECT `destination_name` as name, ifnull(`destination_latitude`,0) as latitude, ifnull(`destination_longtitude`,0) as longtitude, `destination_description` as description, (SELECT display_name from users where users.user_id = destination_poster) as poster, `destination_like` as 'like' FROM `destinations` WHERE 1 order by destination_id desc LIMIT 2;"
    temp_res = execute_select_with_cursor(mycursor, sql_query)
    
    for i in range(len(temp_res)) :
        dest_now = temp_res[i]
        select_destination_img = f"SELECT * FROM `images` WHERE destination_id = (SELECT destination_id from destinations where destination_name = '{dest_now['name']}') LIMIT 1"
        destination_img_now = execute_select_with_cursor(mycursor, select_destination_img)
        if len(destination_img_now) > 0 :
            destination_img_now = destination_img_now[0]
            if (str(destination_img_now['image']).startswith(('http://', 'https://'))) :
                temp_res[i].update({"imageUri":destination_img_now['image']})
            else :
                saved_image_path_now = "https://" + DOMAIN_NOW + "/image/" + dest_now['name']
                temp_res[i].update({"imageUri":saved_image_path_now})
        else :
            temp_res[i].update({"imageUri":"https://demofree.sirv.com/nope-not-here.jpg"})
    
    temp_res = json.dumps(temp_res)
    return temp_res


if __name__ == "__main__":
    ngrok_tunnel = ngrok.connect(PORT, domain= DOMAIN_NOW)  # Assuming Flask app is running on port 5000
    print("Public URL:", ngrok_tunnel.public_url)

    run_flask_app()
    