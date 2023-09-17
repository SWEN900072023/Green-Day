import Form from "../../form-input/form-input";
import Button from "../../button/button";
import { useState } from "react";
const Signin = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const signin = () => {};
  return (
    <div className="sign-up-container">
      {/* <h1>Don't have any account?</h1> */}
      <h2>Sign in with your account!</h2>
      <form onSubmit={signin}>
        <Form
          label="Email"
          type="email"
          required
          onChange={(e) => setEmail(e.target.value)}
          name="email"
          value={email}
        ></Form>
        <Form
          label="Password"
          type="password"
          required
          onChange={(e) => setPassword(e.target.value)}
          name="password"
          value={password}
        ></Form>
        <Button type="submit" buttonType="google">
          Sign in
        </Button>
      </form>
    </div>
  );
};

export default Signin;
