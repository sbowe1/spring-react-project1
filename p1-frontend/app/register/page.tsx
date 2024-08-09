"use client";

import postgres from "postgres";
import { useActionState } from "react";
import { useFormStatus } from "react-dom";
import { createUser } from "./actions";

type Repo = { 
    userId: number, 
    email: string, 
    username: string, 
    roles: string[], 
    plans: string[]
}

const initialState = {
    message: "",
};

let sql = postgres(process.env.DATABASE_URL || process.env.POSTGRES_URL!, {
    ssl: "allow",
});

function RegisterButton() {
    const { pending } = useFormStatus();
  
    return (
      <button type="submit" aria-disabled={pending}>
        Register
      </button>
    );
}


export default async function Register() {
    const [state, formAction] = useActionState(createUser, initialState);

    return (
        <div>
            <h1>Register</h1>
            <form action={formAction}>
                <label htmlFor="email">Email</label>
                <input name="email" type="text" placeholder="email" />

                <label htmlFor="password">Password</label>
                <input name="password" type="password" placeholder="password" />

                <label htmlFor="username">Username</label>
                <input name="username" type="text" placeholder="username" />
                <RegisterButton />
            </form>
        </div>
    )
}