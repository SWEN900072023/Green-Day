import { Fragment } from "react";
import { Outlet, Link } from "react-router-dom";
import "./navigation.style.scss";
import { useDispatch, useSelector } from "react-redux";
import { selectorCurrentUser } from "../../store/user/user.selector";
import { setCurrentUser } from "../../store/user/user.action";
import HomeIcon from "@mui/icons-material/Home";
const Navigation = () => {
  const currentUser = useSelector(selectorCurrentUser);
  const dispatch = useDispatch();
  const Signout = () => {
    dispatch(setCurrentUser(null));
  };
  return (
    <Fragment>
      <div className="navigation">
        {/* TODO:When user login, we display home icon for user back to home page */}
        {currentUser == null ? (
          <h2>Welcome To Music Events System</h2>
        ) : (
          <div className="home-icon">
            <Link className="nav-link" to={"/home"}>
              <HomeIcon />
            </Link>
          </div>
        )}
        <div className="nav-links-container">
          {currentUser == null ? (
            <>
              <Link className="nav-link" to={"/"}>
                Sign up
              </Link>
              <Link className="nav-link" to={"/login"}>
                Sign in
              </Link>
            </>
          ) : (
            <>
              <Link className="nav-link" to={"/"} onClick={Signout}>
                Sign out
              </Link>
              {currentUser.userType === "EventPlanner" ? (
                <Link className="nav-link" to={"/eventCreation"}>
                  Create Event
                </Link>
              ) : currentUser.userType === "Customer" ? (
                <Link className="nav-link" to={"/mybookings"}>
                  Bookings
                </Link>
              ) : (
                <>
                  <Link className="nav-link" to={"/User"}>
                    User Info
                  </Link>
                  <Link className="nav-link" to={"/venueCreation"}>
                    Create Venue
                  </Link>
                </>
              )}
            </>
          )}
        </div>
      </div>
      <Outlet />
    </Fragment>
  );
};

export default Navigation;
