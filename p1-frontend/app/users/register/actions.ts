"use server";

import { revalidatePath } from "next/cache";
import postgres from "postgres";
import { z } from "zod";

let sql = postgres(process.env.DATABASE_URL || process.env.POSTGRES_URL!, {
  ssl: "allow",
});

// CREATE TABLE users (
//   userId SERIAL PRIMARY KEY,
//   email TEXT NOT NULL,
//   password TEXT NOT NULL,
//   username TEXT NOT NULL
// );

export async function createUser(
  prevState: {
    message: string;
  },
  formData: FormData,
) {
  const schema = z.object({
    email: z.string().min(1),
    password: z.string().min(1),
    username: z.string().min(1),
  });
  const parse = schema.safeParse({
    email: formData.get("email"),
    password: formData.get("password"),
    username: formData.get("username")
  });

  if (!parse.success) {
    return { message: "Failed to create user" };
  }

  const data = parse.data;

  try {
    await sql`
      INSERT INTO users (email, password, username)
      VALUES (${data.email}, ${data.password}, ${data.username});
    `;

    revalidatePath("/");
    return { message: `Added user ${data.username}` };
  } catch (e) {
    return { message: "Failed to create user" };
  }
}

export async function deleteUser(
  prevState: {
    message: string;
  },
  formData: FormData,
) {
  const schema = z.object({
    userId: z.string().min(1),
    email: z.string().min(1),
    password: z.string().min(1),
    username: z.string().min(1)
  });
  const data = schema.parse({
    userId: formData.get("userId"),
    email: formData.get("email"),
    password: formData.get("password"),
    username: formData.get("username"),
  });

  try {
    await sql`
      DELETE FROM users
      WHERE userId = ${data.userId};
    `;

    revalidatePath("/");
    return { message: `Deleted user ${data.username}` };
  } catch (e) {
    return { message: "Failed to delete user" };
  }
}
