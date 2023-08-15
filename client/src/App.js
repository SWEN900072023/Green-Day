import { Routes, Route } from "react-router-dom";
import "./App.css";
import Signup from "./components/authentication/signup";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Signup />}>
        {/* <Route></Route> */}
      </Route>
    </Routes>
  );
}

export default App;
