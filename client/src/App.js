import { Routes, Route } from "react-router-dom";
import "./App.css";
import Signup from "./components/routes/authentication/signup";
import Signin from "./components/routes/authentication/signin";
import Navigation from "./components/routes/navigation/navigation";
import Home from "./components/routes/home/home";
import MyBookings from "./components/routes/myBookings/myBookings";
import EventBooking from "./components/eventBooking/eventBooking";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigation />}>
        <Route index element={<Signup />} />
        <Route path="login" element={<Signin />}></Route>
        {/* <Route></Route> */}
        <Route path="home" element={<Home />}></Route>
        <Route path="mybookings" element={<MyBookings />}></Route>
        {/* TODO: EVENT NAME SHOULD BE DISPLAYED ON THE NAVIGATION ROUTE */}
        <Route path={`Summer BBQ/booking`} element={<EventBooking />}></Route>
      </Route>
    </Routes>
  );
}

export default App;
