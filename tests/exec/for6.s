	.text
	.globl main
main:
	subq $8, %rsp
	movq $10, %rdi
	movq %rdi, 0(%rsp)
L16:
	movq 0(%rsp), %rdi
	subq $1, %rdi
	movq %rdi, 0(%rsp)
	addq $1, %rdi
	cmpq $0, %rdi
	je L4
	movq 0(%rsp), %rdi
	addq $65, %rdi
	call putchar
	movq 0(%rsp), %rdi
	subq $1, %rdi
	movq %rdi, 0(%rsp)
	jmp L16
L4:
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $8, %rsp
	ret
	.data
