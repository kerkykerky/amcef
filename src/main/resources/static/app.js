document.getElementById('addPostForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const userId = document.getElementById('userId').value;
    const title = document.getElementById('title').value;
    const body = document.getElementById('body').value;

    const post = {
        userId: parseInt(userId),
        title: title,
        body: body
    };

    fetch('/posts', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(post)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text) });
        }
        return response.json();
    })
    .then(data => {
        alert('Post added successfully!');
        console.log('Success:', data);

        getPostsByUserId(userId);
    })
    .catch(error => {
        alert('Error: ' + error.message);
        console.error('There was a problem with the fetch operation:', error);
    });
});

document.getElementById('findPostForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const userId = document.getElementById('searchUserId').value;

    getPostsByUserId(userId);
});

function getPostsByUserId(userId) {
    fetch(`/posts/user?userId=${userId}`)
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            return response.json();
        })
        .then(data => {
            const postList = document.getElementById('postList');
            postList.innerHTML = '';

            if (data.length === 0) {
                postList.innerHTML = '<li>No posts found.</li>';
            } else {
                data.forEach(post => {
                    const listItem = document.createElement('li');
                    listItem.textContent = `Title: ${post.title}, Body: ${post.body}`;
                    postList.appendChild(listItem);
                });
            }
        })
        .catch(error => {
            alert('Error: ' + error.message);
            console.error('There was a problem with the fetch operation:', error);
        });
}
