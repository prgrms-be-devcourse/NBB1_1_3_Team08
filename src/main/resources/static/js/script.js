const config = {
  projectsPerPage: 8,
  jobsPerPage: 8,
};

let currentProjectsPage = 1;
let currentJobPage = 1;
let isLogin = false;

// 토큰이 필요 없는 함수
async function fetchData(url) {
  const response = await axios.get(url);
  return response.data;
}

// 토큰이 필요한 함수
async function fetchDataWithToken(url, token) {
  const response = await axios.get(url, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
}

async function fetchUserInfo() {
  const token = localStorage.getItem('token');
  if (token) {
    try {
      const userData = await fetchDataWithToken('/api/v1/members', token);
      isLogin = true;
      document.getElementById('welcomeText').innerText = `환영합니다, ${userData.nickname}님!`;

      // 모달에 사용자 정보 설정
      document.getElementById('userNickname').innerText = `닉네임: ${userData.nickname}`;
      document.getElementById('userEmail').innerText = `이메일: ${userData.email}`;
      document.getElementById('userJob').innerText = `직업: ${userData.job}`;
      document.getElementById('userAffiliation').innerText = `소속: ${userData.affiliation}`;
      document.getElementById('userCareer').innerText = `경력: ${userData.career}년`;
      document.getElementById('userSelfIntroduction').innerText = `자기소개: ${userData.self_introduction}`;
      document.getElementById('userGithub').innerHTML = `GitHub: <a href="${userData.github_link}" target="_blank">${userData.github_link}</a>`;
      document.getElementById('userBlog').innerHTML = `블로그: <a href="${userData.blog_link}" target="_blank">${userData.blog_link}</a>`;
      document.getElementById('userInterest').innerText = `관심 분야: ${userData.interest}`;
      document.getElementById('userTechStacks').innerText = `기술 스택: ${userData.tech_stacks.map(stack => stack.name).join(', ')}`;
    } catch (error) {
      console.error('사용자 정보 로드 실패:', error);
    }
  }
}

async function fetchAllProjects(page = 1) {
  const projects = await fetchData(`/api/v1/boards?page=${page - 1}&size=${config.projectsPerPage}`);
  renderProjects(projects.content);

  // 페이지네이션 제어
  document.getElementById('prevPage').style.display = page > 1 ? 'inline' : 'none';
  document.getElementById('nextPage').style.display = page < projects.totalPages ? 'inline' : 'none';
}

async function fetchJobListings(page = 1) {
  const jobListings = await fetchData(`/api/v1/job-posts?page=${page - 1}&size=${config.jobsPerPage}`);
  renderJobs(jobListings.content);

  // 페이지네이션 제어
  document.getElementById('jobPrevPage').style.display = page > 1 ? 'inline' : 'none';
  document.getElementById('jobNextPage').style.display = page < jobListings.totalPages ? 'inline' : 'none';
}


function renderProjects(projects) {
  const allProjectsList = document.getElementById('allProjectsList');
  allProjectsList.innerHTML = projects.map(item => `
        <div class="col-12 col-sm-6 col-md-4 col-lg-3 mb-3">
          <div class="card" onclick="location.href='/boards/${item.board_id}'">
            <div class="card-body">
              <span class="badge ${item.category === 'STUDY' ? 'bg-info' : 'bg-danger'}">${item.category === 'STUDY' ? '스터디' : '프로젝트'}</span>
              <h5 class="card-title">${item.title.length > 15 ? item.title.substring(0, 15) + '...' : item.title}</h5>
              <h6 class="card-subtitle mb-2 text-muted">작성자: ${item.author}</h6>
              <p class="card-text">마감일: ${new Date(item.end_date).toLocaleDateString()}</p>
              <p class="card-text">좋아요: ${item.likes} | 조회수: ${item.views}</p>
              ${item.tech_stacks.slice(0,4).map(tech => `<span class="badge bg-secondary">${tech.name}</span>`).join(' ')}
            </div>
          </div>
        </div>
      `).join('');
}

function renderJobs(jobs) {
  const jobListingsList = document.getElementById('jobListingsList');
  jobListingsList.innerHTML = jobs.map(job => `
        <div class="col-12 col-sm-6 col-md-4 col-lg-3 mb-3">
          <div class="card" onclick="location.href='/job-posts/${job.job_post_id}'">
            <div class="card-body">
              <span class="badge bg-success">채용 공고</span>
              <h5 class="card-title">${job.job_post_name.length > 15 ? job.job_post_name.substring(0, 15) + '...' : job.job_post_name}</h5>
              <h6 class="card-subtitle mb-2 text-muted">회사: ${job.company_name.length > 10 ? job.company_name.substring(0, 10) + '...' : job.company_name}</h6>
              <p class="card-text">마감일: ${new Date(job.end_date).toLocaleDateString()}</p>
              <p class="card-text">좋아요: ${job.likes} | 조회수: ${job.views}</p>
              ${job.tech_stacks.slice(0,4).map(tech => `<span class="badge bg-secondary">${tech.name}</span>`).join(' ')}
            </div>
          </div>
        </div>
      `).join('');
}

document.getElementById('prevPage').addEventListener('click', (event) => {
  event.preventDefault();
  if (currentProjectsPage > 1) {
    currentProjectsPage--;
    fetchAllProjects(currentProjectsPage);
  }
});

document.getElementById('nextPage').addEventListener('click', (event) => {
  event.preventDefault();
  currentProjectsPage++;
  fetchAllProjects(currentProjectsPage);
});

document.getElementById('jobPrevPage').addEventListener('click', (event) => {
  event.preventDefault();
  if (currentJobPage > 1) {
    currentJobPage--;
    fetchJobListings(currentJobPage);
  }
});

document.getElementById('jobNextPage').addEventListener('click', (event) => {
  event.preventDefault();
  currentJobPage++;
  fetchJobListings(currentJobPage);
});

async function logout() {
  const token = localStorage.getItem('token');
  if (token) {
    try {
      await axios.post('/api/v1/members/logout', {}, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      localStorage.removeItem('token');
      window.location.href = '/';
    } catch (error) {
      console.error('로그아웃 실패:', error);
      alert('로그아웃에 실패했습니다. 다시 시도해 주세요.');
    }
  }
}

document.getElementById('logoutButton').addEventListener('click', (event) => {
  event.preventDefault();
  logout();
});

async function fetchNotifications() {
  const token = localStorage.getItem('token');
  if (token) {
    try {
      const { count, alarms } = await fetchDataWithToken('/api/v1/alarms', token);
      document.getElementById('notificationCount').innerText = count;
      renderNotifications(alarms);
    } catch (error) {
      console.error('알림 로드 실패:', error);
    }
  }
}

function renderNotifications(alarms) {
  const notificationContent = document.getElementById('notificationContent');
  notificationContent.innerHTML = '';

  if (alarms.length === 0) {
    notificationContent.innerHTML = '<p>새 알림이 없습니다.</p>';
  } else {
    alarms.forEach(alarm => {
      const notificationItem = document.createElement('p');
      notificationItem.innerText = `${alarm.sender}: ${alarm.content}`;
      notificationContent.appendChild(notificationItem);
    });
  }
}

async function fetchUserInterestBoards() {
  const token = localStorage.getItem('token');
  if (token) {
    try {
      const interests = await fetchDataWithToken('/api/v1/boards/interests', token);
      renderUserInterestBoards(interests);
    } catch (error) {
      console.error('관심 스터디/프로젝트 로드 실패:', error);
    }
  }
}

function renderUserInterestBoards(interests) {
  const userInterestBoardsList = document.getElementById('userInterestBoardsList');
  userInterestBoardsList.innerHTML = interests.slice(0, 4).map(item => `
        <div class="col-12 col-sm-6 col-md-4 col-lg-3 mb-3">
          <div class="card" onclick="location.href='/boards/${item.board_id}'">
            <div class="card-body">
              <span class="badge ${item.category === 'STUDY' ? 'bg-info' : 'bg-danger'}">${item.category === 'STUDY' ? '스터디' : '프로젝트'}</span>
              <h5 class="card-title">${item.title.length > 15 ? item.title.substring(0, 15) + '...' : item.title}</h5>
              <h6 class="card-subtitle mb-2 text-muted">작성자: ${item.author}</h6>
              <p class="card-text">마감일: ${new Date(item.end_date).toLocaleDateString()}</p>
              <p class="card-text">좋아요: ${item.likes} | 조회수: ${item.views}</p>
              ${item.tech_stacks.slice(0,4).map(tech => `<span class="badge bg-secondary">${tech.name}</span>`).join(' ')}
            </div>
          </div>
        </div>
      `).join('');
}

// 토큰이 필요한 함수
async function fetchUserInterestJobPosts() {
  const token = localStorage.getItem('token');
  if (token) {
    try {
      const jobPosts = await fetchDataWithToken('/api/v1/job-posts/interests', token);
      renderUserInterestJobPosts(jobPosts.content);
    } catch (error) {
      console.error('관심 채용 공고 로드 실패:', error);
    }
  }
}

function renderUserInterestJobPosts(jobPosts) {
  const interestJobListings = document.getElementById('interestJobListings');
  interestJobListings.innerHTML = jobPosts.slice(0, 4).map(job => `
      <div class="col-12 col-sm-6 col-md-4 col-lg-3 mb-3">
        <div class="card" onclick="location.href='/job-posts/${job.job_post_id}'">
          <div class="card-body">
            <span class="badge bg-success">채용 공고</span>
            <h5 class="card-title">${job.job_post_name.length > 15 ? job.job_post_name.substring(0, 15) + '...' : job.job_post_name}</h5>
            <h6 class="card-subtitle mb-2 text-muted">회사: ${job.company_name.length > 10 ? job.company_name.substring(0, 10) + '...' : job.company_name}</h6>
            <p class="card-text">마감일: ${new Date(job.end_date).toLocaleDateString()}</p>
            <p class="card-text">좋아요: ${job.likes} | 조회수: ${job.views}</p>
            ${job.tech_stacks.slice(0,4).map(tech => `<span class="badge bg-secondary">${tech.name}</span>`).join(' ')}
          </div>
        </div>
      </div>
    `).join('');
}

async function initialize() {
  await fetchUserInfo();

  if (isLogin) {
    await fetchUserInterestBoards();
    await fetchUserInterestJobPosts();
    await fetchNotifications();
    document.getElementById('logoutItem').style.display = 'block';
    document.getElementById('welcomeMessage').style.display = 'block';
    document.getElementById('userContent').style.display = 'block';
    document.getElementById('myInfo').style.display = 'block';
    document.getElementById('notificationItem').style.display = 'block';
    document.getElementById('myChatRoom').style.display = 'block';

  } else {
    document.getElementById('guestContent').style.display = 'block';
    document.getElementById('loginItem').style.display = 'block';
    document.getElementById('signUpItem').style.display = 'block';
  }

  await Promise.all([
    fetchJobListings(),
    fetchAllProjects(),
    fetchPopularBoards()
  ]);
}

async function fetchPopularBoards() {
  const boards = await fetchData('/api/v1/boards/popular');
  renderPopularBoards(boards.slice(0, 4));
}

function renderPopularBoards(boards) {
  const boardsList = document.getElementById('popularBoardsList');
  boardsList.innerHTML = boards.map(item => `
        <div class="col-12 col-sm-6 col-md-4 col-lg-3 mb-3">
          <div class="card" onclick="location.href='/boards/${item.board_id}'">
            <div class="card-body">
              <span class="badge ${item.category === 'STUDY' ? 'bg-info' : 'bg-danger'}">${item.category === 'STUDY' ? '스터디' : '프로젝트'}</span>
              <h5 class="card-title">${item.title.length > 15 ? item.title.substring(0, 15) + '...'
      : item.title}</h5>
              <h6 class="card-subtitle mb-2 text-muted">작성자: ${item.author}</h6>
              <p class="card-text">마감일: ${new Date(item.end_date).toLocaleDateString()}</p>
              <p class="card-text">좋아요: ${item.likes} | 조회수: ${item.views}</p>
              ${item.tech_stacks.slice(0,4).map(tech => `<span class="badge bg-secondary">${tech.name}</span>`).join(' ')}
            </div>
          </div>
        </div>
      `).join('');
}




