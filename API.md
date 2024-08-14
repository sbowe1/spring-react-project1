# REST APIs

All Request Body inputs should be in JSON form. Outputs will also be returned in JSON form.

*Apart from User Registration and Login, all API requests reqire the JSON Web Token to be sent in the Authorization Header as a Bearer Token.*

<style>
    table th:nth-of-type(4) {
        width: 25%;
    }
</style>

<table>
    <thead>
        <tr>
            <th>Function</th>
            <th>HTTP Method</th>
            <th>URL</th>
            <th>Request Body</th>
            <th>Output</th>
            <th>Notes</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>User Registration</td>
            <td>POST</td>
            <td>{{baseUrl}}/users/register</td>
            <td>
                <pre>{ <br> "email": "value1", <br> "password": "value2", <br> "username": "value3" <br>}</pre>
            </td>
            <td>
                <pre>{ <br> "userId": value, <br> "email": "value1", <br> "username": "value2", <br> "roles": [<br>     "ROLE_USER" <br>  ], <br> "plans": []<br>}</pre>
            </td>
            <td></td>
        </tr>
        <tr>
            <td>Login</td>
            <td>POST</td>
            <td>{{baseUrl}}/users/login</td>
            <td>
                <pre>{<br> "username": "value1", <br> "password": "value2" <br>}</pre>
            </td>
            <td>
                JSON Web Token
            </td>
            <td></td>
        </tr>
        <tr>
            <td>View User Profile</td>
            <td>GET</td>
            <td>{{baseUrl}}/users/profile</td>
            <td>
            </td>
            <td>
                <pre>{ <br> "userId": value, <br> "email": "value1", <br> "username": "value2", <br> "roles": [<br>     "ROLE_USER" <br> ], <br> "plans": [<br>     "Spring Boot Roadmap" <br> ]<br>}</pre>
            </td>
            <td></td>
        </tr>
        <tr>
            <td>View All User Profiles</td>
            <td>GET</td>
            <td>{{baseUrl}}/users</td>
            <td>
            </td>
            <td>
                List of User entities
            </td>
            <td>User must have "ROLE_ADMIN"</td>
        </tr>
        <tr>
            <td>Update User Profile</td>
            <td>PUT</td>
            <td>{{baseUrl}}/users/update</td>
            <td>
                Full or partial User entity, containing fields to be updated
            </td>
            <td>
                Updated JSON Web Token
            </td>
            <td>Roles and Plans must be represented in an array</td>
        </tr>
        <tr>
            <td>Delete User</td>
            <td>DELETE</td>
            <td>{{baseUrl}}/users</td>
            <td>
            </td>
            <td>
                Account deleted successfully!
            </td>
            <td></td>
        </tr>
        <tr>
            <td>Create a Plan</td>
            <td>POST</td>
            <td>{{baseUrl}}/plans/create</td>
            <td>
                Name
            </td>
            <td>
                <pre>{<br> "planId": value, <br> "name": "Name" <br> }</pre>
            </td>
            <td>Name should not be encapsulated in quotes</td>
        </tr>
        <tr>
            <td>View Plan</td>
            <td>GET</td>
            <td>{{baseUrl}}/plans/{planId}</td>
            <td>
            </td>
            <td>
                <pre>{<br> "planId": value, <br> "name": "Name" <br> }</pre>
            </td>
            <td>The planId to look up must be included as a path variable</td>
        </tr>
        <tr>
            <td>View Plan Contents</td>
            <td>GET</td>
            <td>{{baseUrl}}/plans/content/{planId}</td>
            <td>
            </td>
            <td>
                <pre>{<br> "planId": value, <br> "planName": "value1", <br> "content": [<br>     {<br>        "topicId": value, <br>        "title": "value1", <br>        "status": false, <br>        "reources": [<br>            "resourceId": value, <br>            "title": "value1", <br>            "description": "value2", <br>            "type": "value3", <br>            "url": "value4" <br>        ]<br>     }, <br>     {<br>        "subtopicId": value, <br>        "title": "value1", <br>        "status": false, <br>        "reources": [<br>            "resourceId": value, <br>            "title": "value1", <br>            "description": "value2", <br>            "type": "value3", <br>            "url": "value4" <br>         ]<br>      }<br>   ]<br>}</pre>
            </td>
            <td>The planId to look up must be included as a path variable</td>
        </tr>
        <tr>
            <td>Delete Plan</td>
            <td>DELETE</td>
            <td>{{baseUrl}}/plans/{planId}</td>
            <td>
            </td>
            <td>
                Plan successfully deleted
            </td>
            <td>The planId to look up must be included as a path variable</td>
        </tr>
        <tr>
            <td>Create a Topic</td>
            <td>POST</td>
            <td>{{baseUrl}}/topics/create</td>
            <td>
                <pre>{<br> "planName": "Plan", <br> "topicName": "Topic" <br>}</pre>
            </td>
            <td>
                <pre>{<br> "topicId": value, <br> "title": "Topic", <br> "plan": {<br>     "planId": value, <br>     "name": "Plan"<br> }, <br> "status": false <br>}</pre>
            </td>
            <td></td>
        </tr>
        <tr>
            <td>View Topic</td>
            <td>GET</td>
            <td>{{baseUrl}}/topics/{topicId}</td>
            <td>
            </td>
            <td>
                <pre>{<br> "topicId": value, <br> "title": "Topic", <br> "plan": {<br>     "planId": value, <br>     "name": "Plan"<br> }, <br> "status": false <br>}</pre>
            </td>
            <td>The topicId to look up must be included as a path variable</td>
        </tr>
        <tr>
            <td>Update Topic</td>
            <td>PATCH</td>
            <td>{{baseUrl}}/topics/{topicId}</td>
            <td>
            </td>
            <td>
                <pre>{<br> "topicId": value, <br> "title": "Topic", <br> "plan": {<br>     "planId": value, <br>     "name": "Plan"<br> }, <br> "status": true <br>}</pre>
            </td>
            <td>The topicId to look up must be included as a path variable. Used to mark a Topic as complete</td>
        </tr>
        <tr>
            <td>Create a Subtopic</td>
            <td>POST</td>
            <td>{{baseUrl}}/subtopics/create</td>
            <td>
                <pre>{<br> "title": "value1", <br> "description": "value2", <br> "topicTitle": "Topic" <br>}</pre>
            </td>
            <td>
                <pre>{<br> "subtopicId": value, <br> "title": "value1", <br> "description": "value2", <br> "topic": {<br>     "topicId": value, <br>     "title": "value1", <br>     "plan": {<br>        "planId": value, <br>        "name": "value1" <br>     }<br> }, <br> "status": false <br>}</pre>
            </td>
            <td></td>
        </tr>
        <tr>
            <td>View Subtopic</td>
            <td>GET</td>
            <td>{{baseUrl}}/subtopics/{subtopicId}</td>
            <td>
            </td>
            <td>
                <pre>{<br> "subtopicId": value, <br> "title": "value1", <br> "description": "value2", <br> "topic": {<br>     "topicId": value, <br>     "title": "value1", <br>     "plan": {<br>        "planId": value, <br>        "name": "value1" <br>     }<br> }, <br> "status": false <br>}</pre>
            </td>
            <td>The subtopicId to look up must be included as a path variable</td>
        </tr>
        <tr>
            <td>Update Subtopic</td>
            <td>PATCH</td>
            <td>{{baseUrl}}/subtopics/{subtopicId}</td>
            <td>
            </td>
            <td>
                <pre>{<br> "subtopicId": value, <br> "title": "value1", <br> "description": "value2", <br> "topic": {<br>     "topicId": value, <br>     "title": "value1", <br>     "plan": {<br>        "planId": value, <br>        "name": "value1" <br>     }<br> }, <br> "status": true <br>}</pre>
            </td>
            <td>The subtopicId to look up must be included as a path variable. Used to mark a Subtopic as complete</td>
        </tr>
        <tr>
            <td>Create a Resource (No Subtopic)</td>
            <td>POST</td>
            <td>{{baseUrl}}//resources/{topicId}</td>
            <td>
                <pre>{<br> "title": "value1", <br> "description": "value2", <br> "type": "value3", <br> "url": "value4" <br>}</pre>
            </td>
            <td>
                <pre>{<br> "resourceId": value, <br> "title": "value1", <br> "description": "value2", <br> "type": "value3", <br> "url": "value4", <br> "topicName": "value5", <br> "subtopicName": null <br>}</pre>
            </td>
            <td>The topicId must be included as a path variable</td>
        </tr>
        <tr>
            <td>Create a Resource (With Subtopic)</td>
            <td>POST</td>
            <td>{{baseUrl}}/resources/{topicId}/{subtopicId}</td>
            <td>
                <pre>{<br> "title": "value1", <br> "description": "value2", <br> "type": "value3", <br> "url": "value4" <br>}</pre>
            </td>
            <td>
                <pre>{<br> "resourceId": value, <br> "title": "value1", <br> "description": "value2", <br> "type": "value3", <br> "url": "value4", <br> "topicName": "value5", <br> "subtopicName": "value6" <br>}</pre>
            </td>
            <td>The topicId and subtopicId must be included as path variables</td>
        </tr>
        <tr>
            <td>View Resource</td>
            <td>GET</td>
            <td>{{baseUrl}}/resources/{resourceId}</td>
            <td>
            </td>
            <td>
                <pre>{<br> "resourceId": value, <br> "title": "value1", <br> "description": "value2", <br> "type": "value3", <br> "url": "value4", <br> "topicName": "value5", <br> "subtopicName": "value6" <br>}</pre>
            </td>
            <td>The resourceId must be included as a path variable</td>
        </tr>
        <tr>
            <td>Update Resource</td>
            <td>PATCH</td>
            <td>{{baseUrl}}/resources/{resourceId}</td>
            <td>
                Full or partial Resource entity, containing fields to be updated
            </td>
            <td>
                Updated Resource entity
            </td>
            <td>The resourceId must be included as a path variable</td>
        </tr>
        <tr>
            <td>Create Question</td>
            <td>POST</td>
            <td>{{baseUrl}}/questions/create</td>
            <td>
                <pre>{<br> "question": "value1", <br> "answer": "value2", <br> "topicName": "value3" <br>}</pre>
            </td>
            <td>
                <pre>{<br> "questionId": value, <br> "question": "value1", <br> "answer": "value2", <br> "correct": false, <br> "topic": {<br>     "topicId": value, <br>     "title": "value1", <br>     "plan": {<br>        "planId": value, <br>        "name": "value1" <br>     }<br> }, <br> "user": {<br>      "userId": value, <br>      "email": "value1", <br>      "username": "value2", <br>      "roles": [<br>        "ROLE_USER" <br>       ], <br>      "plans": [<br>        "Spring Boot Roadmap" <br>       ]<br> } <br>}</pre>
            </td>
            <td>The resourceId must be included as a path variable</td>
        </tr>
        <tr>
            <td>View Question</td>
            <td>GET</td>
            <td>{{baseUrl}}/questions/{questionId}</td>
            <td>
            </td>
            <td>
                <pre>{<br> "questionId": value, <br> "question": "value1", <br> "answer": "value2", <br> "correct": false, <br> "topic": {<br>     "topicId": value, <br>     "title": "value1", <br>     "plan": {<br>        "planId": value, <br>        "name": "value1" <br>     }<br> }, <br> "user": {<br>      "userId": value, <br>      "email": "value1", <br>      "username": "value2", <br>      "roles": [<br>        "ROLE_USER" <br>       ], <br>      "plans": [<br>        "Spring Boot Roadmap" <br>       ]<br> } <br>}</pre>
            </td>
            <td>The questionId must be included as a path variable</td>
        </tr>
        <tr>
            <td>View All Questions By User</td>
            <td>GET</td>
            <td>{{baseUrl}}/questions/user</td>
            <td>
            </td>
            <td>
                List of Question entities, minus the User information
            </td>
            <td></td>
        </tr>
        <tr>
            <td>View User's Questions Per Topic</td>
            <td>GET</td>
            <td>{{baseUrl}}/questions/topic/{topicId}</td>
            <td>
            </td>
            <td>
                List of Question entities, minus the User and Topic information
            </td>
            <td>The topicId must be included as a path variable</td>
        </tr>
        <tr>
            <td>Mark Question as Correct</td>
            <td>PATCH</td>
            <td>{{baseUrl}}/questions/corect/{questionId}</td>
            <td>
            </td>
            <td>
                <pre>{<br> "questionId": value, <br> "question": "value1", <br> "answer": "value2", <br> "correct": true, <br> "topic": {<br>     "topicId": value, <br>     "title": "value1", <br>     "plan": {<br>        "planId": value, <br>        "name": "value1" <br>     }<br> }, <br> "user": {<br>      "userId": value, <br>      "email": "value1", <br>      "username": "value2", <br>      "roles": [<br>        "ROLE_USER" <br>       ], <br>      "plans": [<br>        "Spring Boot Roadmap" <br>       ]<br> } <br>}</pre>
            </td>
            <td>The questionId must be included as a path variable. Used to track knowledge of Question content</td>
        </tr>
        <tr>
            <td>Update Question</td>
            <td>PUT</td>
            <td>{{baseUrl}}/questions/{questionId}</td>
            <td>
                Full or partial Question entity, containing fields to be updated
            </td>
            <td>
                <pre>{<br> "questionId": value, <br> "question": "value1", <br> "answer": "value2", <br> "correct": true, <br> "topic": {<br>     "topicId": value, <br>     "title": "value1", <br>     "plan": {<br>        "planId": value, <br>        "name": "value1" <br>     }<br> }, <br> "user": {<br>      "userId": value, <br>      "email": "value1", <br>      "username": "value2", <br>      "roles": [<br>        "ROLE_USER" <br>       ], <br>      "plans": [<br>        "Spring Boot Roadmap" <br>       ]<br> } <br>}</pre>
            </td>
            <td>
                The questionId must be included as a path variable. Intended use: correcting typos in question, answer, or URL
            </td>
        </tr>
        <tr>
            <td>Delete Question</td>
            <td>DELETE</td>
            <td>{{baseUrl}}/questions/{questionId}</td>
            <td>
            </td>
            <td>
                <pre>Question: [question] deleted successfully</pre>
            </td>
            <td>
                The questionId must be included as a path variable
            </td>
        </tr>
    </tbody>
</table>