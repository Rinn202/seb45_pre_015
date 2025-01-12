import styled from 'styled-components';
import { fetchUserInfo } from '../util/fetchlogin'
import { useEffect, useState } from 'react';
import React from 'react';

const ButtonAndUser = styled.div`
  .space {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 16px 0;
    padding-top: 10px;
    width: 550px;
  }
  .styled-Button {
  font-size: 13px;
  color: hsl(210,8%,45%);
  cursor: pointer;
  }

  .edited-date {
    font-size: 12px;
    color: hsl(206,100%,40%);
    padding-top: 7px;
  }

  .user-infomation {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 7px 6px 7px 7px;
  border-radius: 3px;

  color: hsl(210,8%,45%);
  font-size: 12px;
  }

  .user-info {
  display: flex;
  align-items: center;
  justify-content: center;
  color: hsl(206,100%,40%);
  }

`

function AnswerUsers() {
  const [userData, setUserData] = useState<any>({});
  const [userProfileImage, setUserProfileImage] = useState<string>('');
  
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const data = await fetchUserInfo();
        setUserData(data);

        if (data.profilePic) {
          setUserProfileImage(data.profilePic);
        }

        sessionStorage.setItem('userEmail', data.email);
        sessionStorage.setItem('accountId', data.accountId);
      } catch (error) {
        console.error('Error while getting user profile:', error);
      }
    };

    fetchUserData();
  }, []);

  const currentDate = new Date().toLocaleDateString();

  return (
    <ButtonAndUser>
              <div className='space'>
          <div className='styled-Button'>
            Share Edit Follow
          </div> 
          <div className='edited-date'>edited {currentDate}</div>
          <div className='asked-users'>
            <div className='user-infomation'>
              <div>asked {currentDate}</div>
              <div className='user-info'>
              <img src={userProfileImage} alt={userData.name} />
                <div>{userData.name}</div>
              </div>
            </div>
          </div>
        </div>
    </ButtonAndUser>
  );
}

export default AnswerUsers;
