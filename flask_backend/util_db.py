import json

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