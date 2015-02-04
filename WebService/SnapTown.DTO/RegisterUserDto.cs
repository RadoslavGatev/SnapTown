using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SnapTown.DTO
{
    public class RegisterUserDto
    {
        public string Name { get; set; }
        public string  FacebookId { get; set; }
        public string GCMClientToken { get; set; }
        public string AuthToken { get; set; }

    }
}
