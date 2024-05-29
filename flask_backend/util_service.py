from PIL import Image, ImageFile
from sentence_transformers import util
import secrets
import string
import requests
import torch
import json

TEMPERATURE_IMG = 0.01
TEMPERATURE_TEXT = 0.01

def load_image(url_or_path):
    if url_or_path.startswith("http://") or url_or_path.startswith("https://"):
        return Image.open(requests.get(url_or_path, stream=True).raw)
    else:
        return Image.open(url_or_path)

def generate_random_string(length=16):
  alphabet = string.ascii_letters + string.digits 
  random_string = ''.join(secrets.choice(alphabet) for _ in range(length))
  return random_string

def convert_tensor_list_to_float(tensor_list) :
    float_list = [float(temp.detach().cpu()) for temp in tensor_list]
    return float_list

def sim_img_destination_sim_dict(destination_dict) :
    similarity_list_now = []
    destination_idx = []
    for destination in destination_dict :
        img_sim_now = destination_dict[destination]["image_sim"]
        if len(img_sim_now) > 0 :
            img_sim_now = torch.stack(img_sim_now)
            similarity_list_now.append(torch.mean(img_sim_now))
            destination_idx.append(destination)
    return destination_idx, similarity_list_now

def sim_text_destination_sim_dict(destination_dict) :
    similarity_list_now = []
    destination_idx = []
    for destination in destination_dict :
        text_sim_now = destination_dict[destination]["text_sim"]
        if len(text_sim_now) > 0 :
            text_sim_now = torch.stack(text_sim_now)
            similarity_list_now.append(torch.mean(text_sim_now))
            destination_idx.append(destination)
    return destination_idx, similarity_list_now

def sim_combine_destination_sim_dict(destination_dict) :
    img_destination_idx, img_similarity_list_now = sim_img_destination_sim_dict(destination_dict)
    text_destination_idx, text_similarity_list_now = sim_text_destination_sim_dict(destination_dict)
    
    img_similarity_list_now = torch.stack(img_similarity_list_now)
    img_similarity_list_mean = torch.mean(img_similarity_list_now)
    text_similarity_list_now = torch.stack(text_similarity_list_now)
    text_similarity_list_mean = torch.mean(text_similarity_list_now)
    
    img_similarity_list_now = []
    txt_similarity_list_now = []
    destination_idx = []
    for destination in destination_dict :
        text_sim_now = destination_dict[destination]["text_sim"]
        img_sim_now = destination_dict[destination]["image_sim"]
        destination_idx.append(destination)
        
        if len(img_sim_now) > 0 :
            img_sim_now = torch.stack(img_sim_now)
            img_similarity_list_now.append(torch.mean(img_sim_now))
        else :
            img_similarity_list_now.append(img_similarity_list_mean)
            
        if len(text_sim_now) > 0 :
            text_sim_now = torch.stack(text_sim_now)
            txt_similarity_list_now.append(torch.mean(text_sim_now))
        else :
            txt_similarity_list_now.append(text_similarity_list_mean)
    img_similarity_list_now = torch.stack(img_similarity_list_now)
    img_similarity_list_now = torch.softmax(img_similarity_list_now/TEMPERATURE_IMG, -1)
    txt_similarity_list_now = torch.stack(txt_similarity_list_now)
    txt_similarity_list_now = torch.softmax(txt_similarity_list_now/TEMPERATURE_TEXT, -1)
    similarity_list_now = (img_similarity_list_now + txt_similarity_list_now)/2
            
    return destination_idx, similarity_list_now
        # text_sim_now = destination_dict["text_sim"]
    
def get_top_k_destination(destination_idx, similarity_list_now, top_k = 3) :
    similarity_list_now = torch.as_tensor(similarity_list_now).float()
    values, indices = similarity_list_now = similarity_list_now.sort(descending=True)
    indices = indices[0:top_k]
    
    inside_str = "("
    for ind in indices :
        inside_str = inside_str + destination_idx[ind] + ","
    inside_str = inside_str[:-1] + ")"
    

    query_now = f"SELECT destination_id as id, `destination_name` as name, ifnull(`destination_latitude`,44) as latitude, ifnull(`destination_longtitude`,66) as longtitude, `destination_description` as description, (SELECT display_name from users where users.user_id = destination_poster) as poster FROM `destinations` WHERE destination_id in {inside_str} order by destination_id desc"
    
    # SELECT name, email
    # FROM Users
    # WHERE id IN (5, 2, 7)
    # ORDER BY FIELD(id, 5, 2, 7
        
    return query_now


def get_top_destination(destination_idx, similarity_list_now) :
    similarity_list_now = torch.as_tensor(similarity_list_now).float()
    best_dest = destination_idx[similarity_list_now.argmax()]
    query_now = f"SELECT * from destinations where destination_id = {best_dest}"
    
    # SELECT name, email
    # FROM Users
    # WHERE id IN (5, 2, 7)
    # ORDER BY FIELD(id, 5, 2, 7
        
    return query_now

def get_destination_sim_dict(all_image_destination_list, all_desc_destination_list, cos_sim_any_img, cos_sim_any_txt) :
    destination_sim_dict = {}
    for temp_img_now_idx in range(len(all_image_destination_list)) :
        dest_now = all_image_destination_list[temp_img_now_idx]
        cos_sim_now = cos_sim_any_img[temp_img_now_idx]
        if dest_now in destination_sim_dict :
            destination_sim_dict[dest_now]["image_sim"].append(cos_sim_now)
        else :
            destination_sim_dict[dest_now] = {
                "image_sim":[cos_sim_now],
                "text_sim":[]
            }
    for temp_desc_now_idx in range(len(all_desc_destination_list)) :
        dest_now = all_desc_destination_list[temp_desc_now_idx]
        cos_sim_now = cos_sim_any_txt[temp_desc_now_idx]
        
        if dest_now in destination_sim_dict :
            destination_sim_dict[dest_now]["text_sim"].append(cos_sim_now)
        else :
            destination_sim_dict[dest_now] = {
                "image_sim":[],
                "text_sim":[cos_sim_now]
            }
    return destination_sim_dict
