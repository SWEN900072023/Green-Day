import { Routes, Route } from "react-router-dom";
import "./App.css";
import Signup from "./components/routes/authentication/signup";
import Signin from "./components/routes/authentication/signin";
import Navigation from "./components/routes/navigation/navigation";
import Home from "./components/routes/home/home";
import MyBookings from "./components/routes/myBookings/myBookings";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigation />}>
        <Route index element={<Signup />} />
        <Route path="login" element={<Signin />}></Route>
        {/* <Route></Route> */}
        <Route path="home" element={<Home />}></Route>
        <Route path="mybookings" element={<MyBookings />}></Route>
      </Route>
    </Routes>
  );
}

export default App;
