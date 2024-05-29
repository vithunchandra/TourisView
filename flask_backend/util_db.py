import json
from PIL import Image
import requests
from io import BytesIO
import os

def execute_select_with_cursor(mycursor, select_query) :
    mycursor.execute(select_query)
    myresult = mycursor.fetchall()
    column_names = [desc[0] for desc in mycursor.description]  # List comprehension for concise extraction

    result = []
    for row in myresult:
        temp_json = {}
        for col_name, col_attr in zip(column_names, row) :
            temp_json.update({
                col_name:col_attr
            })
        result.append(temp_json)
    return result

def execute_query(mydb, mycursor, query) :
    mycursor.execute(query)
    mydb.commit()  
    
    affected_rows = mycursor.rowcount
    
    if affected_rows > 0:
        return "Successfully updated " + str(affected_rows) + " rows"
    else:
        return "No rows were affected"

    

def execute_select_with_cursor_as_json(mycursor, select_query) :
    result = execute_select_with_cursor(mycursor, select_query)
    result_json = json.dumps(result)
    return result_json


def load_image(source):
    if source.startswith(('http://', 'https://')):
        response = requests.get(source)
        response.raise_for_status()  # Ensure we notice bad responses
        img = Image.open(BytesIO(response.content))
    elif os.path.isfile(source):
        img = Image.open(source)
    else:
        raise ValueError("The source must be a valid URL or file path.")
    return img