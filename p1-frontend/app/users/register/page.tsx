import postgres from "postgres";

let sql = postgres(process.env.DATABASE_URL || process.env.POSTGRES_URL!, {
    ssl: "allow",
});

export default async function Register() {
    let todos = await sql`SELECT * FROM todos`;
  
    return (
        <>
            <h1>Register</h1>
            <RegisterForm />
            <ul>
            {todos.map((todo) => (
                <li key={todo.id}>
                {todo.text}
                <DeleteForm id={todo.id} todo={todo.text} />
                </li>
            ))}
            </ul>
        </>
    );
  }