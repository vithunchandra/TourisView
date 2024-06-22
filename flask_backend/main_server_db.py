from pyngrok import ngrok
import requests
import mysql.connector
from flask import Flask, request, send_from_directory, abort
import numpy as np

from sentence_transformers import SentenceTransformer, util

from util_db import *
from datetime import datetime
from util_service import *


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



mycursor.execute("SELECT * FROM images")
myresult_image = mycursor.fetchall()
all_image_list = []
all_image_destination_list = []
for image_data in myresult_image:
    all_image_list.append(load_image(image_data[2]))
    all_image_destination_list.append(image_data[1])
assert len(all_image_list) == len(all_image_destination_list)

mycursor.execute("SELECT * FROM destinations")
myresult_destination = mycursor.fetchall()
all_desc_list = []
all_desc_destination_list = []
for destination_data in myresult_destination:
    desc_now = destination_data[4].replace("/n","\n")
    desc_now_list = desc_now.split("\n\n")
    for desc_now_temp in desc_now_list :
        all_desc_list.append(desc_now_temp)
        all_desc_destination_list.append(destination_data[0])
assert len(all_desc_list) == len(all_desc_destination_list)

model_img = SentenceTransformer('clip-ViT-B-32')
model_text = SentenceTransformer('sentence-transformers/clip-ViT-B-32-multilingual-v1')
# all_img_embeddings = model_img.encode(all_image_list)
# all_desc_embeddings = model_text.encode(all_desc_list)

app = Flask(__name__)

app.config['all_img_embeddings'] = model_img.encode(all_image_list)
app.config['all_desc_embeddings'] = model_text.encode(all_desc_list)


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


@app.route('/image/<filename>/<imgename>', methods=['GET'])
def get_image(filename, imgename):
    print("====================== SEND IMG ======================")
    print(filename, imgename)
    print("=================================-====================")
    picture_exist = os.path.exists(f"./uploaded/{filename}/{imgename}")
    
    if picture_exist:
        return send_from_directory('uploaded', f"./{filename}/{imgename}")  # Specify subfolder
    else :
        return abort(404)

# name = name, imageUri = image,
# description = description, latitude = latitude,
# longitude = longitude, like = 0,
# poster = poster
@app.route('/uploadDestination', methods=['POST'])
def upload_destination() :
    destination_name = request.form.get('name')
    destination_latitude = request.form.get('latitude')
    destination_longitude = request.form.get('longitude')
    destination_description = request.form.get('description')
    destination_location_name = request.form.get('locationName')
    destination_poster_username = request.form.get('poster')

    image = request.files['photo']
    destination_like = 0
    
    
    select_query_str = f"SELECT user_id from users where email = '{destination_poster_username}'"
    destination_poster = execute_select_with_cursor(mycursor, select_query_str)
    if len(destination_poster) < 1  :
        return abort(404, "User not found")
    destination_poster = destination_poster[0]['user_id']
    
    select_query_str = f"SELECT destination_id from destinations where destination_name = '{destination_name}'"
    temp_check_len = execute_select_with_cursor(mycursor, select_query_str)
    if len(temp_check_len) > 0  :
        return abort(400, "Tourism place already exist")
    
    replace_dest_name = str(destination_name).replace(" ","_").lower()
    folder_path_now = f"./uploaded/{replace_dest_name}/"
    if not os.path.exists(folder_path_now):
        os.mkdir(folder_path_now)
    img_path_now = folder_path_now + "img1." + image.filename.split(".")[-1]    
    image.save(img_path_now)
    
    
    sql_query = f"INSERT INTO `destinations`(`destination_name`, `destination_latitude`, `destination_longtitude`, `destination_description`, `destination_poster`, `destination_like`) VALUES (%s,%s,%s,%s,%s,%s)"
    # {destination_name}','{destination_latitude}','{destination_longitude}','{destination_description}','{destination_poster}','{destination_like}
    val_query_now = (destination_name, destination_latitude, destination_longitude, destination_description, destination_poster, destination_like)
    mycursor.execute(sql_query, val_query_now)
    mydb.commit()
    
    inserted_destination = f"SELECT * from destinations where destination_name='{destination_name}'"
    inserted_destination = execute_select_with_cursor(mycursor, inserted_destination)[0]
    
    
    sql_query = f"INSERT INTO `images`(`destination_id`, `image`) VALUES ('{inserted_destination['destination_id']}','{img_path_now}')"
    temp_res = execute_query(mydb=mydb, mycursor=mycursor, query=sql_query)
    
    select_destination = f"SELECT destination_id as id, `destination_name` as name, ifnull(`destination_latitude`,0) as latitude, ifnull(`destination_longtitude`,0) as longtitude, `destination_description` as description, (SELECT email from users where users.user_id = destination_poster) as poster FROM `destinations` WHERE destination_name = '{destination_name}'"
    dst_now = execute_select_with_cursor(mycursor, select_destination)[0]
    
    select_destination_img = f"SELECT * FROM `images` WHERE destination_id = (SELECT destination_id from destinations where destination_name = '{destination_name}') LIMIT 1"
    destination_img_now = execute_select_with_cursor(mycursor, select_destination_img)[0]
    # if not destination_img_now.startswith(('http://', 'https://')) :
    #     return abort(500, "MUST BE URL")
    
    # print(len(all_desc_list), len(all_desc_destination_list),len(all_image_destination_list),len(all_image_list))
    temporary_desc_now = dst_now['description']
    temporary_img_now = load_image(img_path_now)
    temporary_desc_now_embed = model_text.encode([temporary_desc_now])
    temporary_img_now_embed = model_img.encode([temporary_img_now])
    all_desc_list.append(temporary_desc_now)
    all_desc_destination_list.append(dst_now['id'])
    all_image_destination_list.append(dst_now['id'])
    all_image_list.append(temporary_img_now)
    app.config["all_img_embeddings"] = np.concatenate((app.config["all_img_embeddings"], temporary_img_now_embed), 0)
    app.config["all_desc_embeddings"] = np.concatenate((app.config["all_desc_embeddings"], temporary_desc_now_embed), 0)
    
    dst_now.update({"imageUrl":destination_img_now['image']})
    dst_now.update({"isBookmarked":False})
    dst_now.update({"id":str(dst_now['id'])})
    dst_now.update({"locationName":dst_now['name']})
    current_time = datetime.now()
    time_string = current_time.strftime("%d/%m/%Y")  # Format as DD/MM/YYYY
    dst_now.update({"createdAt":time_string})
    
    json_result_now = {
        "message":temp_res,
        "data": dst_now
    }
    
    # print(sql_query)
    return json_result_now


@app.route('/getAllDestinations', methods=['GET'])
def get_all_destinations() :
    sql_query = "SELECT destination_id as id, `destination_name` as name, ifnull(`destination_latitude`,44) as latitude, ifnull(`destination_longtitude`,66) as longtitude, `destination_description` as description, (SELECT display_name from users where users.user_id = destination_poster) as poster FROM `destinations` WHERE 1 order by destination_id desc"
    temp_res = execute_select_with_cursor(mycursor, sql_query)
    
    user_email = request.args.get('email')
    search_name = request.args.get('name')
    print(search_name)
    if (search_name == None) or  (search_name == ""):
        search_name = ""
    else :
        # temp_res2 = []
        # for now_res in temp_res :
        #     if search_name in str(now_res['name']) :
        #         temp_res2.append(now_res)
        # temp_res = temp_res2  
        
        
        text_embeddings = model_text.encode([search_name])
        cos_sim_txt_img = util.cos_sim(text_embeddings, app.config['all_img_embeddings'])[0]
        cos_sim_txt_txt = util.cos_sim(text_embeddings, app.config['all_desc_embeddings'])[0]
        
        destination_sim_dict = get_destination_sim_dict(all_image_destination_list, all_desc_destination_list, cos_sim_txt_img, cos_sim_txt_txt)
        destination_idx, similarity_list_now = sim_combine_destination_sim_dict(destination_sim_dict)
        similarity_list_now = torch.as_tensor(similarity_list_now).float()
        values, indices = similarity_list_now = similarity_list_now.sort(descending=True)
        indices = indices[0:5].detach().cpu().numpy()
        indices = [destination_idx[int(ind)] for ind in  indices]

        print(type(temp_res))
        temp_res2 = []
        for ind in indices :
            for now_res in temp_res :
                print("==================================================")
                print(now_res)
                print("==================================================")
                now_res_id = int(now_res['id'])
                print(now_res_id)
                
                if now_res_id == ind :
                    temp_res2.append(now_res)
        temp_res = temp_res2        
    

     
    for i in range(len(temp_res)) :
        temp_res[i].update({"id":str(temp_res[i]['id'])})
    
        bookmark_now = False
        if not (user_email == None) and not (user_email == ""):
            check_bookmark_query = f"SELECT count(*) as cnt FROM user_destinations where user_id = (SELECT user_id from users where email = '{user_email}') and destination_id = {temp_res[i]['id']}"
            check_bookmark = execute_select_with_cursor(mycursor, check_bookmark_query)[0]['cnt']
            bookmark_now = check_bookmark >= 1
        
        temp_res[i].update({"isBookmarked":bookmark_now})
        temp_res[i].update({"locationName":temp_res[i]['name']})
        current_time = datetime.now()
        time_string = current_time.strftime("%d/%m/%Y")  # Format as DD/MM/YYYY
        temp_res[i].update({"createdAt":time_string}) 
        #description 
        temp_res[i].update({"description":temp_res[i]['description'].replace("/n","\n")}) 
        star_avg_query = f"SELECT ifnull(AVG(star),0) as avg_star from reviews where destination_id = {temp_res[i]['id']}"
        star_avg = execute_select_with_cursor(mycursor, star_avg_query)[0]['avg_star']
        temp_res[i].update({"avgStar":float(star_avg)})
        
        dest_now = temp_res[i]
        select_destination_img = f"""SELECT * FROM `images` WHERE destination_id = (SELECT destination_id from destinations where destination_name = '{dest_now["name"]}') LIMIT 1"""
        destination_img_now = execute_select_with_cursor(mycursor, select_destination_img)
        if len(destination_img_now) > 0 :
            destination_img_now = destination_img_now[0]
            if (str(destination_img_now['image']).startswith(('http://', 'https://'))) :
                temp_res[i].update({"imageUrl":destination_img_now['image']})
            else :
                # saved_image_path_now = "https://" + DOMAIN_NOW + "/image/" + dest_now['name']
                img_path_from_db = str(destination_img_now['image']).split("/")
                saved_image_path_now = "https://" + DOMAIN_NOW + "/image/" + img_path_from_db[2] + "/" + img_path_from_db[3]
                temp_res[i].update({"imageUrl":saved_image_path_now})
        else :
            temp_res[i].update({"imageUrl":"https://demofree.sirv.com/nope-not-here.jpg"})
    
    temp_res = json.dumps(temp_res)
    return temp_res

@app.route('/getAllHistory', methods=['GET'])
def get_all_history() :
    email_now = request.args.get('email')
    query_params = {
        "email": email_now,
    }

    all_destinations = requests.get(f"http://localhost:{PORT}/getAllDestinations",params=query_params).text
    all_destinations = json.loads(all_destinations)
    
    all_history = []
    for dest in all_destinations :
        dest_id = dest["id"]
        poster_now_email_query = f"SELECT email from users where user_id = (SELECT destinations.destination_poster from destinations where destinations.destination_id = {dest_id})"
        poster_now_email = execute_select_with_cursor(mycursor, poster_now_email_query)[0]['email']
        print(dest_id, poster_now_email)
        if poster_now_email == email_now :
            all_history.append(dest)
    all_history = json.dumps(all_history)
    
    return all_history

@app.route('/toggleBookmark', methods=['POST'])
def toggle_bookmark() :
    user_email = request.form.get('reviewer')
    destination_id = request.form.get('destination_id')
    query_now = f"SELECT count(*) as cnt FROM user_destinations where user_id = (SELECT user_id from users where email = '{user_email}') and destination_id = {destination_id}"
    bookmark_now = execute_select_with_cursor(mycursor, query_now)
    
    if(bookmark_now[0]['cnt'] == 0) :
        query_now = f"INSERT INTO `user_destinations`(`user_id`, `destination_id`) VALUES ((SELECT user_id from users where email = '{user_email}') ,{destination_id})"
    else :
        query_now = f"DELETE FROM `user_destinations` WHERE user_id = (SELECT user_id from users where email = '{user_email}')  and destination_id = {destination_id}"
    temp_res = execute_query(mydb, mycursor, query_now)
    return temp_res

@app.route('/insertReview', methods=['POST'])
def write_review() :
    user_email = request.form.get('reviewer')
    destination_id = request.form.get('destination_id')
    review = request.form.get('review')
    star = request.form.get('star')
    val = (user_email, destination_id, review, star)
    
    
    query_now = "INSERT INTO `reviews`(`reviewer`, `destination_id`, `review`, `star`) VALUES ((SELECT user_id from users where email = %s),%s,%s,%s)"
    print(query_now, val)
    mydb.reconnect()
    mycursor.execute(query_now, val)
    mydb.commit()
    
    selected_data_query = f"SELECT review_id, (SELECT display_name from users where user_id = reviewer) as reviewer, destination_id, review, star from reviews where destination_id = {destination_id} and reviewer = (select user_id from users where email =  '{user_email}')"
    selected_data = execute_select_with_cursor(mycursor, selected_data_query)[0]
    
    review_result = {
        "message" : "Suceed!",
        "data" : selected_data
    }
    
    return review_result

@app.route('/getAllReview', methods=['GET'])
def get_all_review() :
    destination_id = request.args.get('destinationId')
    query_now = f"SELECT review_id, (SELECT display_name from users where user_id = reviewer) as reviewer, destination_id, review, star from reviews where destination_id = {destination_id}"
    print("========================= GET ALL REVIEW =================================")
    print(query_now)
    print("==========================================================================")
    return execute_select_with_cursor_as_json(mycursor,query_now)

if __name__ == "__main__":
    ngrok_tunnel = ngrok.connect(PORT, domain= DOMAIN_NOW)  # Assuming Flask app is running on port 5000
    print("Public URL:", ngrok_tunnel.public_url)

    run_flask_app()
    