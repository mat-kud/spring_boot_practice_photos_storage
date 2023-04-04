# Spring boot practice project
------------------------------------------------------------------------
## Photos holder
The goal of the application is to upload and store images and assign them to the selected category.

## Details

Base url: https://localhost:3000  
Only image file formats are accepted, otherwise bad request exception is thrown.  
To add a photo use form on the ```/upload.html```.  
To perform ```DELETE``` method use browser's console or external application like Postman.  
The javascript's code example to ```DELETE``` the photo:
```
fetch('http://localhost:3000/photos/1', { method: 'DELETE' })
```

| Action | Endpoint |
| ------ | ------ |
| Get all photos | /photos |
| Get the photo by id | /photos/{id} |
| Get the photos by category | /photos/filter/category?values={category} |
| Add the photo | /upload.html |
| Delete the photo by id | /photos/{id} |
| Download the photo | /download/{id} |

Photos are stored directly in the database. It's not the best practice, and I'm aware that it's better to store links to the images instead.  