"use client";

import { createUser } from "@/app/users/register/actions";
import { useActionState } from "react";
import { useFormStatus } from "react-dom";

const initialState = {
    message: "",
};

function RegisterButton() {
    const { pending } = useFormStatus();
  
    return (
      <button type="submit" aria-disabled={pending}>
        Register
      </button>
    );
}

export function RegisterForm() {
    const [state, formAction] = useActionState(createUser, initialState);

    return (
        <form action={formAction}>
            <label htmlFor="email">Email</label>
            <input name="email" type="text" placeholder="email" />

            <label htmlFor="password">Password</label>
            <input name="password" type="password" placeholder="password" />

            <label htmlFor="username">Username</label>
            <input name="username" type="text" placeholder="username" />
            <RegisterButton />
        </form>
    )
}