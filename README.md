# RESTful Web Server with MongoDB

---

## Execution Instructions

**How to run with Docker:**

- **X** = mongodbPort/pathToFolder (e.g., "27017/ex3")
- **Y** = js-server port
- **Z** = cpp-server port

1. **Create a new js-server image:**
   ```bash
   cd src/MVC
   docker build --build-arg CONNECTION_STRING="mongodb://host.docker.internal:X" --build-arg PORT=Y --build-arg CPP_PORT=Z -t server .
2. **Create a new js-server container:**
   ```bash
   docker run -d --name myappcontainer -p Y:Y server
3. **Create a new cpp-server image:**
   ```bash
   docker build -f src/recommendationSystem/Dockerfile -t cppserver .
4. **Create a new cpp-server container:**
   ```bash
   docker run -d --name myappcontainer2 -p Z:Z cppserver Z
---

## File Storage Notes
- Data is stored in the data.txt file inside the container. You can initialize it by editing the data/data.txt file before building the image.
- Modifying this file inside the container will alter the stored data. Changing data/data.txt will only affect the initial data during setup
- Note: When running the same container again, the data will remain as it was the last time it was run and will not be reinitialized from your local data/data.txt file.

The format for initialization is:

[userid1] [movieid1] [movieid2] [movieid3]...

[userid2] [movieid1] [movieid2] [movieid3]...

...

- Please note that the movie IDs in each row should be unique, and each user ID should appear only once.

**If you want to take data from the container into your local data/data.txt file run the command:**

`docker cp myappcontainer2:/usr/src/myapp/src/recommendationSystem/data/data.txt src/recommendationSystem/data/data.txt`

# Server-Side Functionality

## Authentication and Registration

### API Endpoints

- **POST `/api/users`**
  - **Description:** Creates a new user. The login form sends user data as JSON in the body.
  - **Example:**
    ```bash
    curl -i -X POST http://localhost:Y/api/users \
    -H "Content-Type: application/json" \
    -d '{"username":"user","password":"1234","gmail":"hemihemi@gmail.com"}'
    ```

- **GET `/api/users/:id`**
  - **Description:** Retrieves user details like name, picture, etc., based on the user ID.
  - **Example:**
    ```bash
    curl -i -X GET http://localhost:Y/api/users/USER_ID
    ```

- **POST `/api/tokens`**
  - **Description:** Validates username and password from JSON in the body, returning a user ID or an error message.
  - **Example:**
    ```bash
    curl -i -X POST http://localhost:Y/api/tokens \
    -H "Content-Type: application/json" \
    -d '{"username":"user","password":"1234"}'
    ```

## Categories and Movies

**Note:** For now, connected users send the `userId` in the header like this `-H "userId:foo"` for requests that can only be done by logged-in users.

### API Endpoints

#### Categories

- **GET `/api/categories`**
  - **Description:** Retrieves all categories.
  - **Example:**
    ```bash
    curl -i -X GET http://localhost:Y/api/categories
    ```

- **POST `/api/categories`**
  - **Description:** Creates a new category.
  - **Example:**
    ```bash
    curl -i -X POST http://localhost:Y/api/categories \
    -H "Content-Type: application/json" \
    -d '{"name":"Action","promoted":true,"description":"Action-packed movies"}'
    ```

- **GET `/api/categories/:id`**
  - **Description:** Fetches a specific category by ID.
  - **Example:**
    ```bash
    curl -i -X GET http://localhost:Y/api/categories/CATEGORY_ID
    ```

- **PATCH `/api/categories/:id`**
  - **Description:** Updates a category by ID.
  - **Example:**
    ```bash
    curl -i -X PATCH http://localhost:Y/api/categories/CATEGORY_ID \
    -H "Content-Type: application/json" \
    -d '{"description":"Updated description"}'
    ```

- **DELETE `/api/categories/:id`**
  - **Description:** Deletes a category by ID.
  - **Example:**
    ```bash
    curl -i -X DELETE http://localhost:Y/api/categories/CATEGORY_ID
    ```

#### Movies

- **GET `/api/movies`**
  - **Description:** Returns a list of movies grouped by categories. Categories marked as "promoted" should return 20 random unwatched movies, plus a special category of the last 20 movies watched by the user in random order.
  - **Example:**
    ```bash
    curl -i -X GET http://localhost:Y/api/movies -H "userId:foo"
    ```

- **POST `/api/movies`**
  - **Description:** Adds a new movie.
  - **Example:**
    ```bash
    curl -i -X POST http://localhost:Y/api/movies \
    -H "Content-Type: application/json" \
    -d '{"title":"Inception","categories":["Sci-Fi","Action"],"description":"Mind-bending thriller","length":"148 minutes","thumbnail":"https://example.com/image.jpg","video":"https://video.com/mp4..mp4"}'
    ```

- **GET `/api/movies/:id`**
  - **Description:** Retrieves details of a specific movie.
  - **Example:**
    ```bash
    curl -i -X GET http://localhost:Y/api/movies/MOVIE_ID
    ```

- **PUT `/api/movies/:id`**
  - **Description:** Recplaces a movie.
  - **Example:**
    ```bash
    curl -i -X PUT http://localhost:Y/api/movies/MOVIE_ID \
    -H "Content-Type: application/json" \
    -d '{"title":"Inception2","categories":["Sci-Fi","Action"],"description":"Mind-bending thriller","length":"148 minutes","thumbnail":"https://example.com/image.jpg","video":"https://video.com/mp4..mp4"}'
    ```

- **DELETE `/api/movies/:id`**
  - **Description:** Deletes a movie.
  - **Example:**
    ```bash
    curl -i -X DELETE http://localhost:Y/api/movies/MOVIE_ID
    ```

## Movie Recommendations

### API Endpoints

- **GET `/api/movies/:id/recommend/`**
  - **Description:** Fetches recommended movies for the current user based on the movie with ID `id`.
  - **Example:**
    ```bash
    curl -i -X GET http://localhost:Y/api/movies/MOVIE_ID/recommend/ -H "userId:foo"
    ```

- **POST `/api/movies/:id/recommend/`**
  - **Description:** Adds viewed movie data to the recommendation system from Exercise 2. Here, your server will act as a client to the recommendation server, using socket communication.
  - **Example:**
    ```bash
    curl -i -X POST http://localhost:Y/api/movies/MOVIE_ID/recommend/ -H "userId:foo"
    ```

### Implementation Details

- Modified the recommendation system to use a `ThreadPool` for handling multiple recommendations.

## Movie Search

### API Endpoint

- **GET `/api/movies/search/:query/`**
  - **Description:** Searches for movies where the query string matches any field in the movie data.
  - **Example:**
    ```bash
    curl -i -X GET http://localhost:Y/api/movies/search/QUERY_STRING/
    ```


# Running Examples

### POST `/api/users` 
![Example 1](https://github.com/user-attachments/assets/b1c8d475-ee85-49a8-9f75-72d2ee640abb)

### GET `/api/users/:id`
![Example 2](https://github.com/user-attachments/assets/cfc40cac-baf5-4449-9c86-ed2a19c08247)

### POST `/api/tokens`
![Example 3](https://github.com/user-attachments/assets/6f42ddc3-a301-4fb7-a382-6a2f29d6cdd5)

### POST `/api/categories`
![Example 4](https://github.com/user-attachments/assets/12c40d2d-5caa-41e4-a38b-636d2de1c851)

### GET `/api/categories`  
![Example 5](https://github.com/user-attachments/assets/a5cca41e-dc16-4e76-a4ad-ec461a97a993)

### GET `/api/categories/:id`
![Example 6](https://github.com/user-attachments/assets/1b93a18f-f29b-4985-91b0-b696b0c2e8c0)

### PATCH `/api/categories/:id`
![Example 7](https://github.com/user-attachments/assets/6e063050-a09a-4e9c-9cff-9060074e0d48)

### POST `/api/movies`
![Example 8](https://github.com/user-attachments/assets/42afabdb-ff53-4941-95c6-58e87cac8b5e)

### GET `/api/movies/:id`
![Example 9](https://github.com/user-attachments/assets/05e7fe03-3e2a-4b58-92f0-c13c76c27c2e)

### POST `/api/movies/:id/recommend/`  
![Example 10](https://github.com/user-attachments/assets/518a3701-bab6-40bc-88ff-bdbe9f5a777d)

### GET `/api/movies/search/:query/`  
![Example 11](https://github.com/user-attachments/assets/ddf56e3b-3ce9-41fe-b6de-6e398db4324f)

### DELETE `/api/movies/:id` 
![Example 12](https://github.com/user-attachments/assets/f69d208a-10d1-4406-90cd-bf6607c7f1e2)

### DELETE `/api/categories/:id`  
![Example 13](https://github.com/user-attachments/assets/3a1aef8c-ab3e-4b65-90c2-d2f3119a8ae3)
  

