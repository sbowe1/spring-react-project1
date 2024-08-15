"use client";
import { useState } from "react";

export function RegisterForm() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [name, setName] = useState("");
    const [message, setMessage] = useState("");

    function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        const formData = new FormData(event.currentTarget);
        try {
            fetch("http://localhost:8080/users/register", {
                headers: {
                    "Content-Type": "application/json",
                },
                method: "POST",
                body: JSON.stringify({
                    email: formData.get("email"),
                    password: formData.get("password"),
                    name: formData.get("name")
                })
            }).then(response => {
                // SECURITY: return 200 regardless of user fields' validities from the server
                if (response.ok) {
                    console.log(response)
                }
                if (response.status === 201) {
                    setMessage("Successfully registered account!")
                    console.log("Created user")
                }
            });
        } catch (error) {
            console.error("Failed to create user", error);
        }
        setEmail("")
        setPassword("")
        setName("")
        setMessage("")
    }

    return (
        <form onSubmit={handleSubmit}>
            <label htmlFor="email">Email</label><br/>
            <input id="email" name="email" type="text" placeholder="email" value={email} onChange={({ target }) => setEmail(target.value)}/><br/><br/>

            <label htmlFor="password">Password</label><br/>
            <input id="password" name="password" type="password" placeholder="password" value={password} onChange={({ target }) => setPassword(target.value)}/><br/><br/>

            <label htmlFor="name">Name</label><br/>
            <input id="name" name="name" type="text" placeholder="name" value={name} onChange={({ target }) => setName(target.value)}/><br/><br/>
            <button type="submit">
                Register
            </button><br/><br/>
            {message != "" ? message : null}
        </form>
    )
}