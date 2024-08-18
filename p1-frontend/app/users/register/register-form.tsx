"use client";
import { Button, Checkbox, FormControlLabel, FormGroup, TextField } from '@mui/material';
import { useState } from "react";

const label = { inputProps: { 'aria-label': 'Checkbox' } };

export function RegisterForm() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [name, setName] = useState("");
    const [message, setMessage] = useState("");
    const [checked, setChecked] = useState(false);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
      setChecked(event.target.checked);
    };

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
                    username: formData.get("name")
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
            <FormGroup >
                <TextField id="name" name="name" placeholder="Miranda Hobbes" label="Name" variant="filled" value={name} onChange={({ target }) => setName(target.value)}/><br/><br/>

                <TextField id="email" name="email" placeholder="mirmir@gmail.com" label="Email" variant="filled"  value={email} onChange={({ target }) => setEmail(target.value)}/><br/><br/>

                <TextField id="password" name="password" type="password" placeholder="Password_" label="Password" variant="filled"  value={password} onChange={({ target }) => setPassword(target.value)}/><br/><br/>
                Requirements<br/>

                <FormControlLabel control={<Checkbox checked={checked} onChange={handleChange} disabled={true}/>} label="8-16 characters" />
                <FormControlLabel control={<Checkbox checked={checked} onChange={handleChange} disabled={true}/>} label={"At least one special character" + "\n" + "! # $ ^ & * - _"} />
                <FormControlLabel control={<Checkbox checked={checked} onChange={handleChange} disabled={true}/>} label="At least one capital letter" />

                <Button type="submit" variant="contained" >Create</Button><br/> {/* // disabled={true} */}
                <Button type="submit" variant="contained">Cancel</Button><br/><br/>
                {message != "" ? message : null}
            </FormGroup>
        </form>
    )
}