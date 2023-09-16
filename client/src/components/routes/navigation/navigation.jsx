import { Fragment } from "react";
import { Outlet, Link } from "react-router-dom";
import "./navigation.style.scss";
const Navigation = () => {
  return (
    <Fragment>
      <div className="navigation">
        {/* TODO:When user login, we display home icon for user back to home page */}
        <h2>Welcome To Music Events System</h2>
        <div className="nav-links-container">
          <Link className="nav-link" to={"/"}>
            Sign up
          </Link>
          <Link className="nav-link" to={"/login"}>
            Sign in
          </Link>
          <Link className="nav-link" to={"/"}>
            Sign out
          </Link>
          {/* TODO: Check user role. If role is user, then display myBooking. If role is event planner, then display create event*/}
          <Link className="nav-link" to={"/mybookings"}>
            Bookings
          </Link>
          <Link className="nav-link" to={"/eventCreation"}>
            Create Event
          </Link>
        </div>
      </div>
      <Outlet />
    </Fragment>
  );
};

export default Navigation;
