"use client";
import { Button, Checkbox, FormControlLabel, FormGroup, TextField } from '@mui/material';
import { useState } from "react";

const specialChars = ["!", "#", "$", "^", "&", "*", "-", "_"];
const emailRegex = new RegExp("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

export function RegisterForm() {
    const [message, setMessage] = useState("");
    const [registerFormFields, setRegisterFormFields] = useState({
        email: "",
        password: "",
        name: ""
    });
    const [emailValidations, setEmailValidations] = useState({
        notEmptyString: false,
        notOnlyWhitespaces: false,
        matchesEmailReqex: false,
        // unique: false,
    });
    const [passwordValidations, setPasswordValidations] = useState({
        length: false,
        specialChar: false,
        capitalLetter: false
    });

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setRegisterFormFields({
            ...registerFormFields,
            [event.target.id]: event.target.value
        })
        if (event.target.id === "name") {
            // query backend for username uniqueness
        };
        if (event.target.id === "email") {
            setRegisterFormFields((prevFields) => {
                setEmailValidations({
                    notEmptyString: event.target.value !== "",
                    notOnlyWhitespaces: event.target.value.trim() !== "",
                    matchesEmailReqex: emailRegex.test(event.target.value),
                    // unique: // query backend for email uniqueness
                })
                return prevFields
            })
        };
        if (event.target.id === "password") {
            setRegisterFormFields((prevFields) => {
                setPasswordValidations({
                    capitalLetter: event.target.value !== event.target.value.toLowerCase(),
                    specialChar: specialChars.some((char) => event.target.value.includes(char)),
                    length: event.target.value.length >= 8 && event.target.value.length <= 16
                })
                return prevFields
            })
        };
    }

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
        setRegisterFormFields({
            email: "",
            password: "",
            name: ""
        })
        setMessage("")
    }

    return (
        <form onSubmit={handleSubmit}>
            
            <FormGroup>
                <TextField
                    style={{backgroundColor: "#E6E0E9"}}
                    id="name"
                    name="name"
                    placeholder="Miranda Hobbes"
                    label="Name"
                    variant="filled"
                    value={registerFormFields.name}
                    required
                    onChange={handleChange}
                /><br/><br/>

                <TextField
                    style={{backgroundColor: "#E6E0E9"}}
                    id="email"
                    name="email"
                    placeholder="mirmir@gmail.com"
                    label="Email"
                    variant="filled"
                    value={registerFormFields.email}
                    required
                    onChange={handleChange}
                /><br/><br/>

                <TextField
                    style={{backgroundColor: "#E6E0E9"}}
                    id="password"
                    name="password"
                    type="password"
                    label="Password"
                    placeholder="Password_"
                    variant="filled"
                    value={registerFormFields.password}
                    required
                    onChange={handleChange}
                /><br/><br/>

                Requirements<br/>

                <FormControlLabel
                    control={
                        <Checkbox checked={passwordValidations.length} disabled={true}
                    />}
                    label="8-16 characters"
                />
                <FormControlLabel
                    control={
                        <Checkbox checked={passwordValidations.specialChar} disabled={true}
                    />}
                    label={"At least one special character" + "\n" + "! # $ ^ & * - _"}
                />
                <FormControlLabel
                    control={
                        <Checkbox checked={passwordValidations.capitalLetter} disabled={true}
                    />}
                    label="At least one capital letter"
                />

                <Button
                    type="submit"
                    variant="contained"
                    disabled={
                        Object.values(emailValidations).every((field) => field === true) 
                        && Object.values(passwordValidations).every((field) => field === true)
                        ? false : true
                    }
                >
                    Create
                </Button><br/> 
                <Button variant="contained">Cancel</Button><br/><br/>
                {message != "" ? message : null}
            </FormGroup>
        </form>
    )
}