new Vue({
    el: '#app',
    data: {
        student: {
            id: null,
            name: ''
        },
        editing: false
    },
    created() {
        const params = new URLSearchParams(window.location.search);
        const studentId = params.get('id');
        if (studentId) {
            this.getStudentById(studentId);
            this.editing = true;
        }
    },
    methods: {
        async getStudentById(studentId) {
            try {
                const response = await axios.get(`/students/${studentId}`);
                this.student = response.data;
            } catch (error) {
                console.error('Error fetching student:', error);
            }
        },
        async createOrUpdateStudent() {
            try {
                if (this.editing) {
                    await axios.put(`/students/${this.student.id}`, this.student);
                } else {
                    await axios.post('/students', this.student);
                }
                window.location.href = 'list.html';
            } catch (error) {
                console.error('Error creating/updating student:', error);
            }
        }
    }
});
